package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MateriaServiceImpl implements MateriaService {
    @Autowired
    private MateriaDao dao;

    @Autowired
    private ProfesorService profesorService;

    @Override
    public Materia crearMateria(MateriaDto materiaDto) throws MateriaServiceException {
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
                throw new MateriaServiceException("Ya existe una Materia con el mismo id.");
            }
        }
        dao.save(m);
        return m;
    }

    @Override
    public List<Materia> getAllMaterias() {
        return new ArrayList<>(dao.getAllMaterias().values());
    }


    @Override
    public Materia getMateriaById(int idMateria) throws MateriaNotFoundException {
        if (dao.getAllMaterias().size()==0) {
            throw new MateriaNotFoundException("No hay materias.");
        }
        return dao.findById(idMateria);
    }

    @Override
    public Materia putMateriaById(int idMateria, MateriaDto materiaDto) throws MateriaNotFoundException, MateriaServiceException {
        checkMateriaDto(materiaDto);
        Materia m = getMateriaById(idMateria);
        m.setAnio(materiaDto.getAnio());
        m.setCuatrimestre(materiaDto.getCuatrimestre());
        m.setNombre(materiaDto.getNombre());
        m.setProfesor(profesorService.buscarProfesor(materiaDto.getProfesorId()));
        return m;
    }

    public Materia delMateriaById(Integer materiaId) throws MateriaNotFoundException {
        if (getAllMaterias().size()>0) {
            for (Materia materia : getAllMaterias()) {
                if (materia.getMateriaId() == materiaId) {
                    dao.del(materia);
                    return materia;
                }
            }
            throw new MateriaNotFoundException("No se encontro la materia con el id: "+materiaId);
        }
        else {
            throw new MateriaNotFoundException("No hay materias.");
        }
    }

    public List<Materia> getAllMateriasSortedBy(String order) throws MateriaNotFoundException, MateriaServiceException {
        if (getAllMaterias().size()>0) {
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
                default -> throw new MateriaServiceException("Especifique el orden.");
            }
        }
        throw new MateriaNotFoundException("No hay materias para ordenar.");
    }

    public List<Materia> getAllMateriasSortedByNameAsc() {
        List<Materia> materias = getAllMaterias();
        materias.sort(Comparator.comparing(Materia::getNombre));
        return materias;
    }
    public List<Materia> getAllMateriasSortedByNameDesc() {
        List<Materia> materias = getAllMaterias();
        materias.sort(Comparator.comparing(Materia::getNombre).reversed());
        return materias;
    }
    public List<Materia> getAllMateriasSortedByCodAsc() {
        List<Materia> materias = getAllMaterias();
        materias.sort(Comparator.comparing(Materia::getMateriaId));
        return materias;
    }
    public List<Materia> getAllMateriasSortedByCodDesc() {
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
        if (!materiaDto.getNombre().matches(".*[a-zA-Z]+.*")) {
            throw new MateriaServiceException("Falta el nombre de la materia");
        }
        else if (materiaDto.getAnio() <= 0) {
            throw new MateriaServiceException("Falta el año de la materia");
        }
        else if (materiaDto.getCuatrimestre() <= 0) {
            throw new MateriaServiceException("Falta el cuatrimestre de la materia");
        }
        else if (materiaDto.getProfesorId() <= 0) {
            throw new MateriaServiceException("Falta el ID del profesor de la materia");
        }
    }
}
