package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.*;
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

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private CarreraService carreraService;

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
        for (Materia materia : dao.getAllMaterias().values()) {if (materia.getMateriaId() == m.getMateriaId()) {throw new MateriaServiceException("Ya existe una Materia con el mismo id.", HttpStatus.CONFLICT);}}
        List<String> materiasListParaCorrelatividades = new ArrayList<>();
        int cantidadMateriasCorrelativas = crearNumeroEntreRangoRandom(0, (asignaturaService.getAllAsignaturas().size()));
        for (Asignatura asignatura : asignaturaService.getAllAsignaturas()) {int numeroRandomMateriaCorrelativa = crearNumeroEntreRangoRandom(0,(asignaturaService.getAllAsignaturas().size()-1));if (!(materiasListParaCorrelatividades.contains(asignatura.getMateria().getNombre())) && materiasListParaCorrelatividades.size()<cantidadMateriasCorrelativas) if (!(asignaturaService.getAllAsignaturas().get(numeroRandomMateriaCorrelativa).getMateria().getNombre().contains(materiaDto.getNombre()))) if (!(materiasListParaCorrelatividades.contains(asignaturaService.getAllAsignaturas().get(numeroRandomMateriaCorrelativa).getMateria().getNombre()))) materiasListParaCorrelatividades.add(asignaturaService.getAllAsignaturas().get(numeroRandomMateriaCorrelativa).getMateria().getNombre());}
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
        if (materias.size()==0) throw new MateriaNotFoundException("No hay materias."); else return materias;
    }

    @Override
    public Materia getMateriaById(int idMateria) throws MateriaNotFoundException {
        return dao.findById(idMateria);
    }

    @Override
    public Materia putMateriaById(int idMateria, MateriaDto materiaDto) throws MateriaNotFoundException, MateriaServiceException, ProfesorNotFoundException {
        //Verificar que obtenemos la materia, y existe
        Materia m1 = getMateriaById(idMateria);
        m1.setAnio(materiaDto.getAnio());
        m1.setCuatrimestre(materiaDto.getCuatrimestre());
        //Chequear que si existe dicha materia, no se actualize con los mismos datos
        checkMateriaDtoPut(m1, materiaDto);
        //Chequear que exista el profesor
        profesorService.buscarProfesor(materiaDto.getProfesorId());

        //Actualizo los profesores
        profesorService.actualizarProfesores(m1.getNombre(), materiaDto.getNombre(), materiaDto.getProfesorId());

        for (Materia materia : dao.getAllMaterias().values()) {
            //Actualizar los profesores de la materia
            if (materia.getMateriaId()==idMateria) {
                materia.setProfesor(profesorService.buscarProfesor(materiaDto.getProfesorId()));
            }
            else {
                materia.setProfesor(profesorService.buscarProfesor(materia.getProfesor().getprofesorId()));
            }

            //Correlativas
            if (!materia.getCorrelatividades().isEmpty()) {
                if (materia.getCorrelatividades().contains(m1.getNombre())) {
                    materia.getCorrelatividades().remove(m1.getNombre());
                    materia.getCorrelatividades().add(materiaDto.getNombre());
                }
            }
        }
        //Actualizar carreras
        carreraService.actualizarProfesoresDeLasCarreras();
        carreraService.actualizarNombreMateriaEnMateriaListDeCarreraYSusCorrelativas(m1.getNombre(), materiaDto.getNombre());
        //Actualizar asignaturas
        asignaturaService.actualizarProfesoresDeLasAsignaturas();
        asignaturaService.actualizarNombreAsignaturaYSusCorrelativas(m1.getNombre(), materiaDto.getNombre());
        //Actualizar alumnos
        alumnoService.actualizarProfesoresDeLasMateriasDeLosAlumnos();
        alumnoService.actualizarNombreMateriaYSusCorrelativasDeLasMateriasDelAlumno(m1.getNombre(), materiaDto.getNombre());

        return getMateriaById(idMateria);
    }

    public Materia delMateriaById(Integer materiaId) throws MateriaNotFoundException, ProfesorNotFoundException {
        if (dao.getAllMaterias().isEmpty()) {
            throw new MateriaNotFoundException("No hay materias.");
        }
        else {
            for (Materia materia : dao.getAllMaterias().values()) {
                if (materia.getMateriaId() == materiaId) {
                    asignaturaService.delAsignaturaByMateria(materia);
                    alumnoService.delMateriaAlumnoByMateriaDel(materia);
                    carreraService.delMateriaEnCarreraByMateria(materia);

                    profesorService.actualizarProfesoresByNombreMateriaDeleted(materia.getNombre());

                    for (Materia m : dao.getAllMaterias().values()) {
                        //Actualizo los profesores de la materia
                        m.setProfesor(profesorService.buscarProfesor(m.getProfesor().getprofesorId()));
                        if (!m.getCorrelatividades().isEmpty()) {
                            m.getCorrelatividades().remove(materia.getNombre());
                        }
                    }
                    //Actualizo profesores
                    carreraService.actualizarProfesoresDeLasCarreras();
                    asignaturaService.actualizarProfesoresDeLasAsignaturas();
                    alumnoService.actualizarProfesoresDeLasMateriasDeLosAlumnos();

                    //Actualizo correlativas
                    carreraService.actualizarCorrelativasCarreraByNameMateriaDeleted(materia.getNombre());
                    asignaturaService.actualizarCorrelativasAsignaturaByNameMateriaDeleted(materia.getNombre());
                    alumnoService.actualizarCorrelativasAlumnoByNameMateriaDeleted(materia.getNombre());

                    dao.del(materia);

                    return materia;
                }
            }
            throw new MateriaNotFoundException("No se encontro la materia con el id: "+materiaId);
        }
    }

    public List<Materia> getAllMateriasSortedBy(String order) throws MateriaServiceException, MateriaNotFoundException {
        if ("nombre_asc".equals(order)) return getAllMateriasSortedByNameAsc();
        else if ("nombre_desc".equals(order)) return getAllMateriasSortedByNameDesc();
        else if ("codigo_asc".equals(order)) return getAllMateriasSortedByCodAsc();
        else if ("codigo_desc".equals(order)) return getAllMateriasSortedByCodDesc();
        else throw new MateriaServiceException("Especifique el orden.", HttpStatus.BAD_REQUEST);
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
        for (Materia materia : materias) if (materia.getNombre().toLowerCase().contains(nombre.toLowerCase())) materiasEncontradas.add(materia);
        if (materiasEncontradas.size()>0) return materiasEncontradas; else throw new MateriaNotFoundException("No hay materias con el nombre: "+nombre);
    }

    public static void checkMateriaDtoPut(Materia materia, MateriaDto materiaDto) throws MateriaServiceException {
        if (materia.getNombre().equals(materiaDto.getNombre()) && materia.getCuatrimestre()==materiaDto.getCuatrimestre() && materia.getAnio()==materiaDto.getAnio() && materia.getProfesor().getprofesorId()==materiaDto.getProfesorId()) throw new MateriaServiceException("Esta materia ya fue actualizada con esos datos", HttpStatus.CONFLICT);
    }

    public void checkMateriaDto(MateriaDto materiaDto) throws MateriaServiceException {
        if (!materiaDto.getNombre().matches(".*[a-zA-Z0-9]+.*")) throw new MateriaServiceException("Falta el nombre de la materia", HttpStatus.UNPROCESSABLE_ENTITY);
        for (Materia materia : dao.getAllMaterias().values()) if (materia.getNombre().equals(materiaDto.getNombre())) throw new MateriaServiceException("Ya existe una materia con el mismo nombre.",HttpStatus.CONFLICT);
        if (materiaDto.getAnio() <= 0) throw new MateriaServiceException("Falta el año de la materia", HttpStatus.UNPROCESSABLE_ENTITY);
        else if (materiaDto.getCuatrimestre() <= 0) throw new MateriaServiceException("Falta el cuatrimestre de la materia", HttpStatus.UNPROCESSABLE_ENTITY);
        else if (materiaDto.getProfesorId() < 0) throw new MateriaServiceException("Falta el ID del profesor de la materia", HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
