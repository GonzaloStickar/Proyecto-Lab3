package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaServiceException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MateriaServiceImpl implements MateriaService {
    @Autowired
    private MateriaDao dao;

    @Autowired
    private ProfesorService profesorService;

    @Autowired
    private AsignaturaService asignaturaService;

    @Override
    public Materia crearMateria(MateriaDto materiaDto) throws MateriaServiceException, ProfesorNotFoundException {
        checkMateriaDto(materiaDto);
        Materia m = new Materia();
        m.setNombre(materiaDto.getNombre());
        m.setAnio(materiaDto.getAnio());
        m.setCuatrimestre(materiaDto.getCuatrimestre());
        m.setProfesor(profesorService.buscarProfesor(materiaDto.getProfesorId()));
        Random random = new Random();
        m.setMateriaId(random.nextInt());
        for (Materia materia : dao.getAllMaterias().values()) {
            if (materia.getMateriaId() == m.getMateriaId()) {
                throw new MateriaServiceException("Ya existe una Materia con el mismo id.", HttpStatus.CONFLICT);
            }
        }
        List<String> materiasListParaCorrelatividades = new ArrayList<>();
        for (Asignatura asignatura : asignaturaService.getAllAsignaturas()) {
            int numeroRandomMateriaCorrelativa = crearNumeroEntreRangoRandom(0,(asignaturaService.getAllAsignaturas().size())-1);
            if (!(materiasListParaCorrelatividades.contains(asignatura.getMateria().getNombre())) && materiasListParaCorrelatividades.size()<3) {
                if (!(asignaturaService.getAllAsignaturas().get(numeroRandomMateriaCorrelativa).getMateria().getNombre().contains(materiaDto.getNombre()))) {
                    if (!(materiasListParaCorrelatividades.contains(asignaturaService.getAllAsignaturas().get(numeroRandomMateriaCorrelativa).getMateria().getNombre()))) {
                        materiasListParaCorrelatividades.add(asignaturaService.getAllAsignaturas().get(numeroRandomMateriaCorrelativa).getMateria().getNombre());
                    }
                }
            }
        }
        m.setCorrelatividades(materiasListParaCorrelatividades);
        asignaturaService.crearAsignatura(m);
        dao.save(m);
        return m;
    }

    public static int crearNumeroEntreRangoRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    @Override
    public List<Materia> getAllMaterias() throws MateriaNotFoundException {
        List<Materia> materias = new ArrayList<>(dao.getAllMaterias().values());
        if (materias.size()==0) {
            throw new MateriaNotFoundException("No hay materias.");
        }
        return materias;
    }

    @Override
    public Materia getMateriaById(int idMateria) throws MateriaNotFoundException {
        return dao.findById(idMateria);
    }

    @Override
    public Materia putMateriaById(int idMateria, MateriaDto materiaDto) throws MateriaNotFoundException, MateriaServiceException, ProfesorNotFoundException {
        checkMateriaDto(materiaDto);
        Materia m = getMateriaById(idMateria);
        m.setAnio(materiaDto.getAnio());
        m.setCuatrimestre(materiaDto.getCuatrimestre());
        m.setNombre(materiaDto.getNombre());
        m.setProfesor(profesorService.buscarProfesor(materiaDto.getProfesorId()));
        asignaturaService.actualizarAsignaturaByMateria(m);
        return m;
    }

    public Materia delMateriaById(Integer materiaId) throws MateriaNotFoundException {
        if (dao.getAllMaterias().values().size()==0) {
            throw new MateriaNotFoundException("No hay materias.");
        }
        else {
            for (Materia materia : dao.getAllMaterias().values()) {
                if (materia.getMateriaId() == materiaId) {
                    asignaturaService.delAsignaturaByMateriaId(materia.getMateriaId());
                    dao.del(materia);
                    return materia;
                }
            }
            throw new MateriaNotFoundException("No se encontro la materia con el id: "+materiaId);
        }
    }

    public List<Materia> getAllMateriasSortedBy(String order) throws MateriaServiceException, MateriaNotFoundException {
        switch (order) {
            case "nombre_asc" -> {
                return getAllMateriasSortedByNameAsc();
            }
            case "nombre_desc" -> {
                return getAllMateriasSortedByNameDesc();
            }
            case "codigo_asc" -> {
                return getAllMateriasSortedByCodAsc();
            }
            case "codigo_desc" -> {
                return getAllMateriasSortedByCodDesc();
            }
            default -> throw new MateriaServiceException("Especifique el orden.", HttpStatus.BAD_REQUEST);
            }
    }

    public List<Materia> getAllMateriasSortedByNameAsc() throws MateriaNotFoundException {
        List<Materia> materias = getAllMaterias();
        materias.sort(Comparator.comparing(Materia::getNombre));
        return materias;
    }
    public List<Materia> getAllMateriasSortedByNameDesc() throws MateriaNotFoundException {
        List<Materia> materias = getAllMaterias();
        materias.sort(Comparator.comparing(Materia::getNombre).reversed());
        return materias;
    }
    public List<Materia> getAllMateriasSortedByCodAsc() throws MateriaNotFoundException {
        List<Materia> materias = getAllMaterias();
        materias.sort(Comparator.comparing(Materia::getMateriaId));
        return materias;
    }
    public List<Materia> getAllMateriasSortedByCodDesc() throws MateriaNotFoundException {
        List<Materia> materias = getAllMaterias();
        materias.sort(Comparator.comparing(Materia::getMateriaId).reversed());
        return materias;
    }

    public List<Materia> getAllMateriasByName(String nombre) throws MateriaNotFoundException {
        List<Materia> materias = getAllMaterias();
        List<Materia> materiasEncontradas = new ArrayList<>();
        for (Materia materia : materias) {
            if (materia.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                materiasEncontradas.add(materia);
            }
        }
        if (materiasEncontradas.size()>0) {
            return materiasEncontradas;
        }
        else {
            throw new MateriaNotFoundException("No hay materias con el nombre: "+nombre);
        }
    }

    public void checkMateriaDto(MateriaDto materiaDto) throws MateriaServiceException {
        if (!materiaDto.getNombre().matches(".*[a-zA-Z0-9]+.*")) {
            throw new MateriaServiceException("Falta el nombre de la materia", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        else if (materiaDto.getAnio() <= 0) {
            throw new MateriaServiceException("Falta el año de la materia", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        else if (materiaDto.getCuatrimestre() <= 0) {
            throw new MateriaServiceException("Falta el cuatrimestre de la materia", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        else if (materiaDto.getProfesorId() <= 0) {
            throw new MateriaServiceException("Falta el ID del profesor de la materia", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
