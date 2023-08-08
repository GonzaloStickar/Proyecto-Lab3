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
        return aprobarAsignatura(idAlumno, idAsignatura, asignaturaDto.getNota());
    }

    @Override
    public Alumno aprobarAsignatura(int idAlumno, int idAsignatura, int nota) throws AlumnoNotFoundException, AsignaturaNotFoundException, AlumnoServiceException, AsignaturaServiceException {
        Alumno alumno = alumnoDao.findById(idAlumno);
        if (alumno.getAsignaturas().isEmpty()) {
            throw new AsignaturaServiceException("El alumno no tiene asignaturas", HttpStatus.NOT_FOUND);
        }
        Asignatura asignatura=null;
        for (Asignatura a : alumno.getAsignaturas()) {
            if (a.getMateria().getMateriaId()==idAsignatura) {
                asignatura = a;
                break;
            }
        }
        if (asignatura==null) {
            throw new AsignaturaNotFoundException("El alumno no tiene registro de la asignatura con id: "+idAsignatura);
        }
        else {
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

        return new ArrayList<>(asignaturasSinDuplicado);
    }

    public Asignatura buscarAsignaturaPorNombre(String nombreCorrelativa) {
        for (Asignatura asignatura : dao.getAllAsignaturas()) {
            if (asignatura.getMateria().getNombre().equals(nombreCorrelativa)) {
                return asignatura;
            }
        }
        return null;
    }

    public List<Asignatura> checkAsignaturaCorrelativas(Asignatura asignatura, List<Asignatura> listaAsignaturasExtraCursadasAprobadas) {
        if (!listaAsignaturasExtraCursadasAprobadas.contains(asignatura)) {
            Asignatura asignaturaCopia = new Asignatura(asignatura.getMateria()); // Crear una copia de la asignatura

            if (asignaturaCopia.getEstado() != EstadoAsignatura.APROBADA) {
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
                    } else {
                        asignaturaCopia.cursarAsignatura();
                    }
                } else {
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
