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
            siAprobadasCorrelativasAsignaturaCorrelativas(a, asignaturasSinDuplicado);
            siCorrelativasAprobadasAsignaturaAprobada(a, asignaturasSinDuplicado);
        }

        return new ArrayList<>(asignaturasSinDuplicado);
    }

    public void siCorrelativasAprobadasAsignaturaAprobada(Asignatura a, List<Asignatura> asignaturasSinDuplicado) {
        if (a.getEstado()!=EstadoAsignatura.NO_CURSADA) {
            boolean puedeCursar=true;
            boolean puedeAprobar=true;
            for (String correlativa : a.getMateria().getCorrelatividades()) {
                Asignatura asignaturaCorrelativa = buscarAsignaturaPorNombreAsignaturas(correlativa, asignaturasSinDuplicado);
                if (asignaturaCorrelativa!=null) {
                    if (!(asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.APROBADA))) {
                        puedeAprobar=false;
                    }
                    if (!(asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.CURSADA))) {
                        puedeCursar=false;
                    }
                    if (!puedeAprobar && !puedeCursar) {
                        break;
                    }
                }
            }
            if (puedeAprobar) {
                int numero = crearNumeroEntreRangoRandom(4,10);
                a.setNota(numero);
                if (numero>=6) {
                    a.aprobarAsignatura();
                }
                else {
                    a.cursarAsignatura();
                }
            }
            else {
                if (puedeCursar) {
                    int numero = crearNumeroEntreRangoRandom(4,5);
                    a.cursarAsignatura();
                    a.setNota(numero);
                }
                else {
                    a.setNota(0);
                    a.setEstado(EstadoAsignatura.NO_CURSADA);
                }
            }
        }
    }

    public void siAprobadasCorrelativasAsignaturaCorrelativas(Asignatura a, List<Asignatura> asignaturasSinDuplicado) {
        boolean puedeAprobar=true;
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
        if (a.getEstado() != EstadoAsignatura.APROBADA) {
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
                a.setNota(0);
                a.setEstado(EstadoAsignatura.NO_CURSADA);
            }
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
