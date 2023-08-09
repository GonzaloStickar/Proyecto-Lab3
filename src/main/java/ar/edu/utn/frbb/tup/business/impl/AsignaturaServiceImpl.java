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
    public Alumno putAsignatura(int idAlumno, int idAsignatura, AsignaturaDto asignaturaDto) throws AlumnoNotFoundException, AsignaturaServiceException, AlumnoServiceException {
        checkAsignaturaDto(asignaturaDto);
        return aprobarAsignatura(idAlumno, idAsignatura, asignaturaDto.getNota());
    }

    public static Asignatura buscarAsignaturaByNameAsignaturasAlumno(Alumno alumno, String nombreAsignaturaABuscar) {
        for (Asignatura asignatura : alumno.getAsignaturas()) {
            if (asignatura.getNombreAsignatura().equals(nombreAsignaturaABuscar)) {
                return asignatura;
            }
        }
        return null;
    }

    @Override
    public Alumno aprobarAsignatura(int idAlumno, int idAsignatura, int nota) throws AlumnoNotFoundException, AlumnoServiceException, AsignaturaServiceException {
        Alumno alumno = alumnoDao.findById(idAlumno);
        if (alumno.getAsignaturas().isEmpty()) {
            throw new AsignaturaServiceException("El alumno no tiene asignaturas", HttpStatus.NOT_FOUND);
        }
        for (Asignatura a : alumno.getAsignaturas()) {
            if (a.getMateria().getMateriaId() == idAsignatura) {
                if (a.getEstado().equals(EstadoAsignatura.CURSADA)) {
                    for (String correlatividad : a.getMateria().getCorrelatividades()) {
                        Asignatura asignaturaCorrelativa = buscarAsignaturaByNameAsignaturasAlumno(alumno, correlatividad);
                        if (asignaturaCorrelativa != null) {
                            if (!asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.APROBADA)) {
                                throw new AlumnoServiceException("La materia " + correlatividad + " debe estar aprobada", HttpStatus.OK);
                            }
                            if (!asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.CURSADA)) {
                                throw new AlumnoServiceException("La materia " + correlatividad + " debe estar cursada", HttpStatus.OK);
                            }
                        }
                    }
                }
                else {
                    throw new AlumnoServiceException("Esta materia ya está aprobada", HttpStatus.BAD_REQUEST);
                }
            }
            if (nota>=0 && nota <=10) {
                if (nota>=6) {
                    a.aprobarAsignatura();
                }
                else {
                    a.cursarAsignatura();
                }
            }
            else {
                throw new AlumnoServiceException("La nota debe ser entre 0 y 10", HttpStatus.BAD_REQUEST);
            }
        }
        return alumno;
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

        List<Asignatura> asignaturasTemporal = new ArrayList<>(dao.getAllAsignaturas());
        for (Asignatura asignatura : asignaturasTemporal) {
            asignatura.setEstado(EstadoAsignatura.NO_CURSADA);
        }

        Asignatura asignatura = asignaturasTemporal.get(crearNumeroEntreRangoRandom(0, asignaturasTemporal.size() - 1));
        List<Asignatura> asignaturasConCorrelativas = new ArrayList<>();
        List<Asignatura> asignaturas = checkAsignaturaCorrelativas(asignatura, asignaturasConCorrelativas);
        for (Asignatura a : asignaturas) {
            checkAsignaturaCorrelativas(a, asignaturasConCorrelativas);
        }

        List<Asignatura> asignaturasSinDuplicado = new ArrayList<>();
        for (Asignatura a : asignaturas) {
            boolean aniadirAsignatura=true;
            Asignatura asignaturaNueva = new Asignatura(a.getMateria());
            asignaturaNueva.setEstado(a.getEstado());
            asignaturaNueva.setNota(a.getNota());
            for (Asignatura asignaturaMateria : asignaturasSinDuplicado) {
                if (asignaturaMateria.getMateria().equals(asignaturaNueva.getMateria())) {
                    aniadirAsignatura = false;
                    break;
                }
            }
            if (aniadirAsignatura) {
                asignaturasSinDuplicado.add(asignaturaNueva);
            }
        }

        for (Asignatura a : asignaturasSinDuplicado) {
            boolean puedeAprobar=true;
//            checkAsignaturaCorrelativasV2(a,asignaturasSinDuplicado);
            if (!(a.getMateria().getCorrelatividades().isEmpty())) {
                for (String correlativa : a.getMateria().getCorrelatividades()) {
                    Asignatura asignaturaCorrelativa = buscarAsignaturaPorNombreAsignaturas(correlativa,asignaturasSinDuplicado);
                    if (asignaturaCorrelativa!=null) {
                        if (asignaturaCorrelativa.getEstado() != EstadoAsignatura.NO_CURSADA) {
                            if (!(asignaturaCorrelativa.getMateria().getCorrelatividades().isEmpty())) {
                                for (String correlativaAsignaturaCorrelativa : asignaturaCorrelativa.getMateria().getCorrelatividades()) {
                                    Asignatura asignaturaCorrelativaAsignaturaCorrelativa = buscarAsignaturaPorNombreAsignaturas(correlativaAsignaturaCorrelativa, asignaturasSinDuplicado);
                                    if (asignaturaCorrelativaAsignaturaCorrelativa!=null) {
                                        if (!(asignaturaCorrelativaAsignaturaCorrelativa.getEstado().equals(EstadoAsignatura.APROBADA))) {
                                            puedeAprobar=false;
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            puedeAprobar=false;
                            break;
                        }
                    }
                }
            }
            if (puedeAprobar) {
                int numero = crearNumeroEntreRangoRandom(0,1);
                if (numero==1) {
                    a.aprobarAsignatura();
                }
                else {
                    a.cursarAsignatura();
                }
            }
            else {
                a.setEstado(EstadoAsignatura.NO_CURSADA);
            }
        }

        return new ArrayList<>(asignaturasSinDuplicado);
    }

    public Asignatura buscarAsignaturaPorNombreAsignaturas(String correlativaNombre, List<Asignatura> listaAsignaturas) {
        for (Asignatura asignatura : listaAsignaturas) {
            if (asignatura.getNombreAsignatura().equals(correlativaNombre)) {
                return asignatura;
            }
        }
        return null;
    }

    public List<Asignatura> checkAsignaturaCorrelativas(Asignatura asignatura, List<Asignatura> listaAsignaturasExtraCursadasAprobadas) {
        if (!listaAsignaturasExtraCursadasAprobadas.contains(asignatura)) {
            Asignatura asignaturaCopia = new Asignatura(asignatura.getMateria()); // Crear una copia de la asignatura

            if (asignaturaCopia.getEstado() != EstadoAsignatura.APROBADA) { // Utilizado para que no pueda aprobar si no tiene las correlativas aprobadas
                boolean todasCorrelativasCursadasAprobadas = true;
                for (String nombreCorrelativa : asignaturaCopia.getMateria().getCorrelatividades()) {
                    Asignatura asignaturaCorrelativa = buscarAsignaturaPorNombre(nombreCorrelativa);
                    if (asignaturaCorrelativa.getEstado() != EstadoAsignatura.APROBADA) {
                        todasCorrelativasCursadasAprobadas = false;
                        break;
                    }
                }
                if (todasCorrelativasCursadasAprobadas) {
                    int numero = crearNumeroEntreRangoRandom(4, 10);
                    asignaturaCopia.setNota(numero);
                    if (numero >= 6) {
                        asignaturaCopia.aprobarAsignatura();
                    }
                    else {
                        asignaturaCopia.cursarAsignatura();
                    }
                }
                else {
                    int numero = crearNumeroEntreRangoRandom(4, 5);
                    asignaturaCopia.setNota(numero);
                    asignaturaCopia.cursarAsignatura();
                }
            }

            listaAsignaturasExtraCursadasAprobadas.add(asignaturaCopia);
            for (String nombreCorrelativa : asignaturaCopia.getMateria().getCorrelatividades()) {
                Asignatura correlativa = buscarAsignaturaPorNombre(nombreCorrelativa);
                if (correlativa != null) {
                    checkAsignaturaCorrelativas(correlativa, listaAsignaturasExtraCursadasAprobadas);
                }
            }
        }
        return listaAsignaturasExtraCursadasAprobadas;
    }

//    public List<Asignatura> checkAsignaturaCorrelativasV2(Asignatura asignatura, List<Asignatura> listaAsignaturasExtraCursadasAprobadas) {
//        if (!listaAsignaturasExtraCursadasAprobadas.contains(asignatura)) {
//            Asignatura asignaturaCopia = new Asignatura(asignatura.getMateria());
//            boolean todasAprobadas = true;
//            for (String nombreCorrelativa : asignaturaCopia.getMateria().getCorrelatividades()) {
//                Asignatura correlativa = buscarAsignaturaPorNombre(nombreCorrelativa);
//                if (correlativa.getEstado() != EstadoAsignatura.APROBADA) {
//                    asignaturaCopia.setEstado(EstadoAsignatura.NO_CURSADA);
//                    todasAprobadas = false;
//                    break;
//                }
//
//                for (String nombreCorrelativaAsignatura : correlativa.getMateria().getCorrelatividades()) {
//                    Asignatura correlativaCorrelativa = buscarAsignaturaPorNombre(nombreCorrelativaAsignatura);
//                    if (correlativaCorrelativa == null || correlativaCorrelativa.getEstado() != EstadoAsignatura.APROBADA) {
//                        asignaturaCopia.setEstado(EstadoAsignatura.NO_CURSADA);
//                        todasAprobadas = false;
//                        break;
//                    }
//                }
//                if (!todasAprobadas) {
//                    break;
//                }
//            }
//
//            if (asignaturaCopia.getEstado() != EstadoAsignatura.APROBADA) {
//                int numero = crearNumeroEntreRangoRandom(4, 10);
//                asignaturaCopia.setNota(numero);
//                if (todasAprobadas) {
//                    if (numero >= 6) {
//                        asignaturaCopia.aprobarAsignatura();
//                    } else {
//                        asignaturaCopia.cursarAsignatura();
//                    }
//                } else {
//                    asignaturaCopia.setEstado(EstadoAsignatura.NO_CURSADA);
//                }
//            }
//
//            listaAsignaturasExtraCursadasAprobadas.add(asignaturaCopia);
//            for (String nombreCorrelativa : asignaturaCopia.getMateria().getCorrelatividades()) {
//                Asignatura correlativa = buscarAsignaturaPorNombre(nombreCorrelativa);
//                if (correlativa != null) {
//                    checkAsignaturaCorrelativas(correlativa, listaAsignaturasExtraCursadasAprobadas);
//                }
//            }
//        }
//        return listaAsignaturasExtraCursadasAprobadas;
//    }

    public Asignatura buscarAsignaturaPorNombre(String nombreCorrelativa) {
        for (Asignatura asignatura : dao.getAllAsignaturas()) {
            if (asignatura.getMateria().getNombre().equals(nombreCorrelativa)) {
                return asignatura;
            }
        }
        return null;
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
