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
                if (a.getEstado() != (EstadoAsignatura.APROBADA)) {
                    for (String correlativa : a.getMateria().getCorrelatividades()) {
                        Asignatura asignaturaCorrelativa = buscarAsignaturaByNameAsignaturasAlumno(alumno, correlativa);
                        if (asignaturaCorrelativa != null) {
                            if (!asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.APROBADA)) {
                                throw new AlumnoServiceException("La materia " + correlativa + " debe estar aprobada", HttpStatus.OK);
                            }
                            if (!asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.CURSADA)) {
                                throw new AlumnoServiceException("La materia " + correlativa + " debe estar cursada", HttpStatus.OK);
                            }
                        }
                    }
                }
                else {
                    throw new AlumnoServiceException("Esta materia ya está aprobada", HttpStatus.BAD_REQUEST);
                }
            }
            a.setNota(nota);
            if (nota>=6) {
                a.aprobarAsignatura();
            }
            else {
                a.cursarAsignatura();
            }
        }
        return alumno;
    }

    public static void checkAsignaturaDto(AsignaturaDto asignaturaDto) throws AsignaturaServiceException {
        if (asignaturaDto.getNota() < 0 || asignaturaDto.getNota()>10) {
            throw new AsignaturaServiceException("Falta la nota de la asignatura, debe estar entre 0 y 10", HttpStatus.UNPROCESSABLE_ENTITY);
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

        List<Asignatura> asignaturasSinDuplicado = new ArrayList<>();
        for (Asignatura a : asignaturas) {
            boolean asignaturaDuplicada = false;
            for (Asignatura asignaturaEnListaSinDuplicado : asignaturasSinDuplicado) {
                if (a.getMateria().equals(asignaturaEnListaSinDuplicado.getMateria())) {
                    if (a.getEstado().equals(EstadoAsignatura.APROBADA) && asignaturaEnListaSinDuplicado.getEstado().ordinal() < EstadoAsignatura.APROBADA.ordinal()) {
                        asignaturaEnListaSinDuplicado.setEstado(EstadoAsignatura.APROBADA);
                    }
                    else if (a.getEstado().equals(EstadoAsignatura.CURSADA) && asignaturaEnListaSinDuplicado.getEstado().equals(EstadoAsignatura.NO_CURSADA)) {
                        asignaturaEnListaSinDuplicado.setEstado(EstadoAsignatura.CURSADA);
                    }
                    asignaturaDuplicada = true;
                    break;
                }
            }
            if (!asignaturaDuplicada) {
                asignaturasSinDuplicado.add(a);
            }
        }

        for (Asignatura a : asignaturasSinDuplicado) {
            siCorrelativasAprobadasAsignaturaAprobada(a, asignaturasSinDuplicado);
        }
        for (Asignatura a : asignaturasSinDuplicado) {
            siAprobadasCorrelativasAsignaturaCorrelativas(a, asignaturasSinDuplicado);
        }

        return new ArrayList<>(asignaturasSinDuplicado);
    }

    public void siCorrelativasAprobadasAsignaturaAprobada(Asignatura a, List<Asignatura> asignaturasSinDuplicado) {
        if (a.getEstado() == EstadoAsignatura.CURSADA || a.getEstado() == EstadoAsignatura.APROBADA) {
            boolean todasCorrelativasCursadas = true;
            boolean todasCorrelativasAprobadas = true;
            for (String correlativa : a.getMateria().getCorrelatividades()) {
                Asignatura asignaturaCorrelativa = buscarAsignaturaPorNombreAsignaturas(correlativa, asignaturasSinDuplicado);
                if (asignaturaCorrelativa == null) {
                    todasCorrelativasCursadas = false;
                    todasCorrelativasAprobadas = false;
                    break;
                }
                if (asignaturaCorrelativa.getEstado() != EstadoAsignatura.CURSADA) {
                    todasCorrelativasCursadas = false;
                }
                if (asignaturaCorrelativa.getEstado() != EstadoAsignatura.APROBADA) {
                    todasCorrelativasAprobadas = false;
                }
            }
            if (a.getEstado() == EstadoAsignatura.CURSADA) {
                if (!todasCorrelativasCursadas) {
                    a.setNota(0);
                    a.setEstado(EstadoAsignatura.NO_CURSADA);
                }
                else {
                    a.setNota(crearNumeroEntreRangoRandom(4, 5));
                }
            }
            else if (a.getEstado() == EstadoAsignatura.APROBADA) {
                if (!todasCorrelativasAprobadas) {
                    a.setNota(crearNumeroEntreRangoRandom(4, 5));
                    a.setEstado(EstadoAsignatura.CURSADA);
                }
                else {
                    a.setNota(crearNumeroEntreRangoRandom(6, 10));
                }
            }
        }
        else {
            a.setNota(0);
            a.setEstado(EstadoAsignatura.NO_CURSADA);
        }
    }

    public void siAprobadasCorrelativasAsignaturaCorrelativas(Asignatura a, List<Asignatura> asignaturasSinDuplicado) {
        boolean puedeCursarCorrelativas = true;
        if (!a.getMateria().getCorrelatividades().isEmpty()) {
            for (String correlativa : a.getMateria().getCorrelatividades()) {
                Asignatura asignaturaCorrelativa = buscarAsignaturaPorNombreAsignaturas(correlativa, asignaturasSinDuplicado);
                if (asignaturaCorrelativa == null || asignaturaCorrelativa.getEstado() != EstadoAsignatura.APROBADA) {
                    puedeCursarCorrelativas = false;
                    break;
                }
                for (String correlativaCorrelativa : asignaturaCorrelativa.getMateria().getCorrelatividades()) {
                    Asignatura asignaturaCorrelativaCorrelativa = buscarAsignaturaPorNombreAsignaturas(correlativaCorrelativa, asignaturasSinDuplicado);
                    if (asignaturaCorrelativaCorrelativa == null || asignaturaCorrelativaCorrelativa.getEstado() != EstadoAsignatura.APROBADA) {
                        puedeCursarCorrelativas = false;
                        break;
                    }
                }
            }
        }

        if (a.getEstado() != EstadoAsignatura.APROBADA) {
            if (puedeCursarCorrelativas) {
                a.setNota(crearNumeroEntreRangoRandom(4, 5));
                a.cursarAsignatura();
            }
            else {
                a.setNota(0);
                a.setEstado(EstadoAsignatura.NO_CURSADA);
            }
        }
        else {
            a.setNota(crearNumeroEntreRangoRandom(6,10));
        }
    }

    public List<Asignatura> checkAsignaturaCorrelativas(Asignatura asignatura, List<Asignatura> listaAsignaturasExtraCursadasAprobadas) {
        if (!listaAsignaturasExtraCursadasAprobadas.contains(asignatura)) {
            Asignatura asignaturaCopia = new Asignatura(asignatura.getMateria());
            setEstadoRandom(asignaturaCopia);
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

    public static void setEstadoRandom(Asignatura asignatura) {
        int numero = crearNumeroEntreRangoRandom(0,2);
        if (numero>0) {
            int nota = crearNumeroEntreRangoRandom(4,10);
            asignatura.setNota(nota);
            if (numero==1) {
                asignatura.aprobarAsignatura();
            }
            else {
                asignatura.cursarAsignatura();
            }
        }
        else {
            asignatura.setNota(0);
            asignatura.setEstado(EstadoAsignatura.NO_CURSADA);
        }
    }

    public Asignatura buscarAsignaturaPorNombreAsignaturas(String correlativaNombre, List<Asignatura> listaAsignaturas) {
        for (Asignatura asignatura : listaAsignaturas) {
            if (asignatura.getNombreAsignatura().equals(correlativaNombre)) {
                return asignatura;
            }
        }
        return null;
    }

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
