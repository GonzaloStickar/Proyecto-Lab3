package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AsignaturaServiceImpl implements AsignaturaService {

    @Autowired
    private AlumnoDao alumnoDao;

    @Autowired
    private AsignaturaDao dao;

    public void crearAsignatura(Materia materia) {
        if (!(materia.getProfesor().getMateriasDictadas().contains(materia.getNombre()))) {
            materia.getProfesor().agregarMateriaDictada(materia.getNombre());
        }
        dao.save(materia);
    }

    @Override
    public Asignatura getAsignatura(int materiaId) throws AsignaturaNotFoundException {
        return dao.getAsignaturaById(materiaId);
    }

    @Override
    public Alumno putAsignatura(int idAlumno, int idAsignatura, AsignaturaDto asignaturaDto) throws AlumnoNotFoundException, AsignaturaNotFoundException, AsignaturaServiceException, AlumnoServiceException {
        checkAsignaturaDto(asignaturaDto);
        if (dao.getAllAsignaturas().isEmpty()) {
            throw new AsignaturaServiceException("No hay asignaturas", HttpStatus.NOT_FOUND);
        }
        return aprobarAsignatura(idAlumno, idAsignatura, asignaturaDto.getNota());
    }

    @Override
    public Alumno aprobarAsignatura(int idAlumno, int idAsignatura, int nota) throws AlumnoNotFoundException, AsignaturaNotFoundException, AlumnoServiceException {
        Alumno alumno = alumnoDao.findById(idAlumno);
        Asignatura asignatura = getAsignatura(idAsignatura);
        List<Asignatura> asignaturasAlumnoList = new ArrayList<>(alumno.getAsignaturas());

        List<String> correlatividadesAsignatura = getAsignatura(idAsignatura).getMateria().getCorrelatividades();

        if (asignaturasAlumnoList.contains(asignatura)) {
            if (!(asignatura.getEstado().equals(EstadoAsignatura.APROBADA))) {
                if (asignaturasAlumnoList.size()>2) {
                    for (Asignatura a : asignaturasAlumnoList) {
                        if (a.equals(asignatura)) {
                            for (String correlatividad : correlatividadesAsignatura) {
                                Asignatura asignaturaCorrelativa = dao.getAsignaturaByName(correlatividad);
                                if (nota>=6) {
                                    if (asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.APROBADA)) {
                                        asignatura.aprobarAsignatura();
                                    }
                                    else {
                                        throw new AlumnoServiceException("La materia " + correlatividad + " debe estar aprobada", HttpStatus.OK);
                                    }
                                }
                                else {
                                    if (asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.CURSADA)) {
                                        asignatura.cursarAsignatura();
                                    } else {
                                        throw new AlumnoServiceException("La materia " + correlatividad + " debe estar cursada", HttpStatus.OK);
                                    }
                                }
                                asignatura.setNota(nota);
                            }
                        }
                    }
                }
                else {
                    if (nota>=6) {
                        asignatura.aprobarAsignatura();
                    }
                    else {
                        asignatura.cursarAsignatura();
                    }
                    asignatura.setNota(nota);
                }
            }
            else {
                throw new AlumnoServiceException("Esta materia ya está aprobada", HttpStatus.OK);
            }
            return alumno;
        }
        else {
            throw new AlumnoServiceException("El alumno no tiene registro de la asignatura con id: " + idAsignatura, HttpStatus.NOT_FOUND);
        }
    }

    public static void checkAsignaturaDto(AsignaturaDto asignaturaDto) throws AsignaturaServiceException {
        if (asignaturaDto.getNota() < 0 || asignaturaDto.getNota()>10) {
            throw new AsignaturaServiceException("Falta la nota de la asignatura", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public List<Asignatura> getSomeAsignaturaRandomFromAsignaturasDao() throws AsignaturaNotFoundException {
        if (dao.getAllAsignaturas().isEmpty()) {
            throw new AsignaturaNotFoundException("No hay Asignaturas");
        }
        Asignatura asignatura = dao.getAllAsignaturas().get(crearNumeroEntreRangoRandom(0,(dao.getAllAsignaturas().size())-1));
        List<Asignatura> asignaturasConCorrelativas = new ArrayList<>();
        List<Asignatura> asignaturas = new ArrayList<>(checkAsignaturaCorrelativas(asignatura,asignaturasConCorrelativas));
        for (Asignatura a : asignaturas) {
            checkAsignaturaCorrelativas(a,asignaturasConCorrelativas);
        }
        return asignaturas;
    }

    public Asignatura buscarAsignaturaPorNombre(String nombreCorrelativa) {
        for (Asignatura asignatura : getAllAsignaturas()) {
            if (asignatura.getMateria().getNombre().equals(nombreCorrelativa)) {
                return asignatura;
            }
        }
        return null;
    }

    public List<Asignatura> checkAsignaturaCorrelativas(Asignatura asignatura, List<Asignatura> listaAsignaturasExtraCursadasAprobadas) {
        if (!listaAsignaturasExtraCursadasAprobadas.contains(asignatura)) {
            if (asignatura.getEstado() != EstadoAsignatura.APROBADA) {
                boolean todasCorrelativasCursadasAprobadas = true;
                for (String nombreCorrelativa : asignatura.getMateria().getCorrelatividades()) {
                    Asignatura asignaturaCorrelativa = buscarAsignaturaPorNombre(nombreCorrelativa);
                    if (asignaturaCorrelativa.getEstado() != EstadoAsignatura.APROBADA) {
                        todasCorrelativasCursadasAprobadas = false;
                        break;
                    }
                }
                int numero = crearNumeroEntreRangoRandom(4, 10);
                asignatura.setNota(numero);
                if (todasCorrelativasCursadasAprobadas) {
                    if (numero >= 6) {
                        asignatura.aprobarAsignatura();
                    }
                    else {
                        asignatura.cursarAsignatura();
                    }
                }
                else {
                    asignatura.cursarAsignatura(); // Cambio aquí, asignatura solo se cursa
                }
            }
            listaAsignaturasExtraCursadasAprobadas.add(asignatura);
            for (String nombreCorrelativa : asignatura.getMateria().getCorrelatividades()) {
                Asignatura correlativa = buscarAsignaturaPorNombre(nombreCorrelativa);
                if (correlativa != null) {
                    checkAsignaturaCorrelativas(correlativa, listaAsignaturasExtraCursadasAprobadas);
                }
            }
        }
        return listaAsignaturasExtraCursadasAprobadas;
    }

    public List<Asignatura> getAllAsignaturas() {
        return dao.getAllAsignaturas();
    }

    public static int crearNumeroEntreRangoRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public void actualizarAsignaturaByMateria(Materia materia) {
        for (Asignatura asignatura : getAllAsignaturas()) {
            if (asignatura.getMateria().equals(materia)) {
                  asignatura.setMateria(materia);
            }
        }
    }

    public void delAsignaturaByMateria(Materia materia) throws MateriaNotFoundException {
        if (dao.getAllAsignaturas().isEmpty()) {
            throw new MateriaNotFoundException("No hay materias");
        }
        else {
            List<Asignatura> asignaturasEliminar = new ArrayList<>();
            for (Asignatura asignatura : dao.getAllAsignaturas()) {
                if (asignatura.getMateria().equals(materia)) {
                    asignaturasEliminar.add(asignatura);
                }
            }
            if (!asignaturasEliminar.isEmpty()) {
                for (Asignatura asignaturaAEliminar : asignaturasEliminar) {
                    dao.del(asignaturaAEliminar.getMateria());
                }
            }
            else {
                throw new MateriaNotFoundException("No se encontró la materia con el id: "+materia.getMateriaId());
            }
        }
    }
}
