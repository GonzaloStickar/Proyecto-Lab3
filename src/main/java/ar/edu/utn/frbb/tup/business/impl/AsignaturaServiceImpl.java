package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
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

    @Autowired
    private ProfesorService profesorService;

    public void crearAsignatura(Materia materia) {
        if (!(materia.getProfesor().getMateriasDictadas().contains(materia.getNombre()))) {materia.getProfesor().agregarMateriaDictada(materia.getNombre());}
        dao.save(new Asignatura(materia));
    }

    @Override
    public Alumno putAsignatura(int idAlumno, int idAsignatura, AsignaturaDto asignaturaDto) throws AlumnoNotFoundException, AsignaturaServiceException, AlumnoServiceException, AsignaturaNotFoundException {
        checkAsignaturaDto(asignaturaDto);
        return aprobarAsignatura(idAlumno, idAsignatura, asignaturaDto.getNota());
    }

    public static Asignatura buscarAsignaturaByNameAsignaturasAlumno(Alumno alumno, String nombreAsignaturaABuscar) {
        for (Asignatura asignatura : alumno.getAsignaturas()) {
            if (asignatura.getNombreAsignatura().equals(nombreAsignaturaABuscar)) {
                return asignatura;}}return null;
    }

    @Override
    public Alumno aprobarAsignatura(int idAlumno, int idAsignatura, int nota) throws AlumnoNotFoundException, AlumnoServiceException, AsignaturaServiceException, AsignaturaNotFoundException {
        Alumno alumno = alumnoDao.findById(idAlumno);
        if (alumno.getAsignaturas().isEmpty()) {throw new AsignaturaServiceException("El alumno no tiene asignaturas", HttpStatus.NOT_FOUND);}

        Asignatura a = null;
        for (Asignatura asignatura : alumno.getAsignaturas()) {
            if (asignatura.getMateria().getMateriaId() == idAsignatura) {
                a = asignatura;
                break;
            }
        }

        if (a != null) {
            if (a.getEstado() != (EstadoAsignatura.APROBADA)) {
                for (String correlativa : a.getMateria().getCorrelatividades()) {
                    Asignatura asignaturaCorrelativa = buscarAsignaturaByNameAsignaturasAlumno(alumno, correlativa);
                    if (asignaturaCorrelativa != null) {
                        if (nota<6) {if (!asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.CURSADA) && !asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.APROBADA)) {throw new AlumnoServiceException("La materia " + correlativa + " debe estar cursada", HttpStatus.OK);} else {a.setNota(nota);a.cursarAsignatura();}}
                        else {if (!asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.APROBADA)) {throw new AlumnoServiceException("La materia " + correlativa + " debe estar aprobada", HttpStatus.OK);} else {a.setNota(nota);a.aprobarAsignatura();}}}}}
            else {
                throw new AlumnoServiceException("Esta materia ya está aprobada", HttpStatus.BAD_REQUEST);
            }
            a.setNota(nota);
            if (nota>=6) {
                a.aprobarAsignatura();} else {a.cursarAsignatura();}
        }
        else {
            throw new AsignaturaNotFoundException("El alumno no tiene registro de la asignatura: "+idAsignatura);
        }
        return alumno;
    }

    public static void checkAsignaturaDto(AsignaturaDto asignaturaDto) throws AsignaturaServiceException {
        if (asignaturaDto.getNota() < 0 || asignaturaDto.getNota()>10) {throw new AsignaturaServiceException("Falta la nota de la asignatura, debe estar entre 0 y 10", HttpStatus.UNPROCESSABLE_ENTITY);}
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
        List<Asignatura> asignaturas = checkAsignaturaCorrelativas(asignatura, new ArrayList<>());

        for (Asignatura a : asignaturas) {
            verificador(a, asignaturas);
        }

        return new ArrayList<>(asignaturas);
    }

    public void verificador(Asignatura asignatura, List<Asignatura> asignaturasSinDuplicado) {
        if (asignatura.getEstado() == EstadoAsignatura.CURSADA) {
            boolean todasLasCorrelatividadesDeSusCorrelatividadesAprobadas = true;
            boolean todasLasCorrelatividadesCursadasOAprobadas = true;
            if (!(asignatura.getMateria().getCorrelatividades().isEmpty())) {
                for (String nombreCorrelativaAsignatura : asignatura.getMateria().getCorrelatividades()) {
                    Asignatura asignaturaCorrelativa = buscarAsignaturaPorNombreAsignaturas(nombreCorrelativaAsignatura, asignaturasSinDuplicado);
                    if (asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.CURSADA) || asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.APROBADA)) {
                        if (!(asignaturaCorrelativa.getMateria().getCorrelatividades().isEmpty())) {
                            for (String nombreCorrelativaDeCorrelativaDeAsignatura : asignaturaCorrelativa.getMateria().getCorrelatividades()) {
                                Asignatura asignaturaCorrelativaDeCorrelativaDeAsignatura = buscarAsignaturaPorNombreAsignaturas(nombreCorrelativaDeCorrelativaDeAsignatura, asignaturasSinDuplicado);
                                if (asignaturaCorrelativaDeCorrelativaDeAsignatura.getEstado() != EstadoAsignatura.APROBADA) {todasLasCorrelatividadesDeSusCorrelatividadesAprobadas = false;break;}}
                        }
                    }
                    else {
                        todasLasCorrelatividadesCursadasOAprobadas=false;
                        break;
                    }
                }
            }
            if (!todasLasCorrelatividadesDeSusCorrelatividadesAprobadas || !todasLasCorrelatividadesCursadasOAprobadas) {
                asignatura.setNota(0);
                asignatura.setEstado(EstadoAsignatura.NO_CURSADA);
            }
        }
        else if (asignatura.getEstado() == EstadoAsignatura.APROBADA) {
            boolean todasLasCorrelatividadesDeSusCorrelatividadesAprobadas = true;
            boolean todasLasCorrelatividadesAprobadas = true;
            if (!(asignatura.getMateria().getCorrelatividades().isEmpty())) {
                for (String nombreCorrelativaAsignatura : asignatura.getMateria().getCorrelatividades()) {
                    Asignatura asignaturaCorrelativa = buscarAsignaturaPorNombreAsignaturas(nombreCorrelativaAsignatura, asignaturasSinDuplicado);
                    if (asignaturaCorrelativa.getEstado().equals(EstadoAsignatura.APROBADA)) {
                        if (!(asignaturaCorrelativa.getMateria().getCorrelatividades().isEmpty())) {
                            for (String nombreCorrelativaDeCorrelativaDeAsignatura : asignaturaCorrelativa.getMateria().getCorrelatividades()) {
                                Asignatura asignaturaCorrelativaDeCorrelativaDeAsignatura = buscarAsignaturaPorNombreAsignaturas(nombreCorrelativaDeCorrelativaDeAsignatura, asignaturasSinDuplicado);
                                if (asignaturaCorrelativaDeCorrelativaDeAsignatura.getEstado() != EstadoAsignatura.APROBADA) {todasLasCorrelatividadesDeSusCorrelatividadesAprobadas = false;break;}
                            }
                        }
                    }
                    else {
                        todasLasCorrelatividadesAprobadas=false;
                        break;
                    }
                }
            }
            if (!todasLasCorrelatividadesDeSusCorrelatividadesAprobadas || !todasLasCorrelatividadesAprobadas) {
                asignatura.setNota(0);
                asignatura.setEstado(EstadoAsignatura.NO_CURSADA);
            }
        }
    }

    public List<Asignatura> checkAsignaturaCorrelativas(Asignatura asignatura, List<Asignatura> listaAsignaturasExtraCursadasAprobadas) {
        boolean agregarAsignatura = true;
        Asignatura asignaturaCopia = new Asignatura(asignatura.getMateria());
        for (Asignatura nombreAsignatura : listaAsignaturasExtraCursadasAprobadas) {if (nombreAsignatura.getNombreAsignatura().equals(asignaturaCopia.getNombreAsignatura())) {agregarAsignatura=false;}}
        if (agregarAsignatura) {
            setEstadoRandom(asignaturaCopia);
            listaAsignaturasExtraCursadasAprobadas.add(asignaturaCopia);
            for (String nombreCorrelativa : asignaturaCopia.getMateria().getCorrelatividades()) {
                Asignatura correlativa = buscarAsignaturaPorNombre(nombreCorrelativa);
                if (correlativa != null) {checkAsignaturaCorrelativas(correlativa, listaAsignaturasExtraCursadasAprobadas);}
            }
        }

        return listaAsignaturasExtraCursadasAprobadas;
    }

    public static void setEstadoRandom(Asignatura asignatura) {
        int numero = crearNumeroEntreRangoRandom(0,2);
        if (numero>0) {
            int nota = crearNumeroEntreRangoRandom(4,10);
            asignatura.setNota(nota);
            if (numero==1 || nota>=6) {
                asignatura.aprobarAsignatura();asignatura.setNota(crearNumeroEntreRangoRandom(6,10));} else {asignatura.cursarAsignatura();}} else {asignatura.setNota(0);asignatura.setEstado(EstadoAsignatura.NO_CURSADA);}
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
                return asignatura;}}
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
        for (Asignatura asignatura : getAllAsignaturas()) {if (asignatura.getMateria().getMateriaId()==(materia.getMateriaId())) {asignatura.setMateria(materia);}}
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

    public void actualizarNombreAsignaturaYSusCorrelativas(String nombreMateriaViejo, String nombreMateriaNuevo) {
        for (Asignatura asignatura : dao.getAllAsignaturas()) {
            //Actualizar nombre de la asignatura por el nombre de la materia
            if (asignatura.getMateria().getNombre().equals(nombreMateriaViejo)) {
                asignatura.getMateria().setNombre(nombreMateriaNuevo);
            }
            //Correlativas
            if (!asignatura.getMateria().getCorrelatividades().isEmpty()) {
                if (asignatura.getMateria().getCorrelatividades().contains(nombreMateriaViejo)) {
                    asignatura.getMateria().getCorrelatividades().remove(nombreMateriaViejo);
                    asignatura.getMateria().getCorrelatividades().add(nombreMateriaNuevo);
                }
            }
        }
    }

    public void actualizarProfesoresDeLasAsignaturas() throws ProfesorNotFoundException {
        for (Asignatura asignatura : dao.getAllAsignaturas()) {
            asignatura.getMateria().setProfesor(profesorService.buscarProfesor(asignatura.getMateria().getProfesor().getprofesorId()));
        }
    }
}