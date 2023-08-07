package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public Alumno putAsignatura(int idAlumno, int idAsignatura, AsignaturaDto asignaturaDto) throws AlumnoNotFoundException, AsignaturaNotFoundException, AsignaturaServiceException, AlumnoServiceException, EstadoIncorrectoException {
        checkAsignaturaDto(asignaturaDto);
        return aprobarAsignatura(idAlumno, idAsignatura, asignaturaDto.getNota());
    }

    @Override
    public Alumno aprobarAsignatura(int idAlumno, int idAsignatura, int nota) throws AlumnoNotFoundException, AsignaturaNotFoundException, AlumnoServiceException, EstadoIncorrectoException {
        Alumno alumno = alumnoDao.findById(idAlumno);
        Asignatura asignatura = getAsignatura(idAsignatura);

        List<Asignatura> asignaturasAlumnoList = new ArrayList<>(alumno.getAsignaturas());

        if (asignaturasAlumnoList.contains(asignatura)) {
            for (Asignatura a : asignaturasAlumnoList) {
                if (a.equals(asignatura)) {
                    a.aprobarAsignatura(nota);
                    break;
                }
            }
            alumno.setAsignaturas(asignaturasAlumnoList);
            return alumno;
        }
        else {
            throw new AlumnoServiceException("El alumno no tiene registro de la asignatura con id: " + idAsignatura, HttpStatus.NOT_FOUND);
        }
    }

    public static void checkAsignaturaDto(AsignaturaDto asignaturaDto) throws AsignaturaServiceException {
        if (asignaturaDto.getNota() <= 0 || asignaturaDto.getNota()>10) {
            throw new AsignaturaServiceException("Falta la nota de la asignatura", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public List<Asignatura> getSomeAsignaturaRandomFromAsignaturasDao() throws AsignaturaNotFoundException {
        if (dao.getAllAsignaturas().isEmpty()) {
            throw new AsignaturaNotFoundException("No hay Asignaturas"); //Lo cual no va a suceder, ya que las Asignaturas estan "Hardcodeadas".
        }
        Asignatura asignatura = dao.getAllAsignaturas().get(crearNumeroEntreRangoRandom(0,(dao.getAllAsignaturas().size())-1));
        List<Asignatura> asignaturasConCorrelativas = new ArrayList<>();
        checkAsignaturaCorrelativas(asignatura, estadoRandomAsignaturaSet(asignaturasConCorrelativas));

        for (Asignatura a : asignaturasConCorrelativas) {
            if (a.getEstado().equals(EstadoAsignatura.APROBADA)) {
                a.setNota(crearNumeroEntreRangoRandom(6,10));
            }
            if (a.getEstado().equals(EstadoAsignatura.CURSADA)) {
                a.setNota(crearNumeroEntreRangoRandom(0,5));
            }
        }

        if (asignaturasConCorrelativas.size()>2) {
            //Se puede hacer Random, primero voy a probar que no pueda Aprobar la Asignatura (Random "asignada),
            //para que tire una exception que diga "No puede Aprobar esta materia si no tiene Aprobadas las correlativas
            //correlativa 1 y correlativa 2 ó correlativa 3 (en caso de que tenga una materia creada por nosotros.
            asignaturasConCorrelativas.get(1).cursarAsignatura();
            asignaturasConCorrelativas.get((asignaturasConCorrelativas.size()) - 1).cursarAsignatura();
            asignaturasConCorrelativas.get(1).setNota(crearNumeroEntreRangoRandom(0,5));
            asignaturasConCorrelativas.get((asignaturasConCorrelativas.size()) - 1).setNota(crearNumeroEntreRangoRandom(0,5));
        }

        asignatura.setEstado(EstadoAsignatura.NO_CURSADA);
        asignatura.setNota(0);
        return asignaturasConCorrelativas;
    }

    public List<Asignatura> checkAsignaturaCorrelativas(Asignatura asignatura, List<Asignatura> listaAsignaturasExtraCursadasAprobadas) {
        if (!listaAsignaturasExtraCursadasAprobadas.contains(asignatura)) {
            if (asignatura.getEstado().equals(EstadoAsignatura.NO_CURSADA)) {
                asignatura.aprobarAsignatura();
            }
            listaAsignaturasExtraCursadasAprobadas.add(asignatura);
        }
        for (String nombreCorrelativa : asignatura.getMateria().getCorrelatividades()) {
            for (Asignatura asignaturaBuscar : getAllAsignaturas()) {
                if (asignaturaBuscar.getMateria().getNombre().equals(nombreCorrelativa)) {
                    checkAsignaturaCorrelativas(asignaturaBuscar, listaAsignaturasExtraCursadasAprobadas);
                }
            }
        }
        return listaAsignaturasExtraCursadasAprobadas;
    }

    public static List<Asignatura> estadoRandomAsignaturaSet(List<Asignatura> asignaturasConCorrelativas) {
        if (asignaturasConCorrelativas.size()>2) {
//            int numero1 = crearNumeroEntreRangoRandom(0, 1);
//            if (numero1 == 1) {
//                asignaturasConCorrelativas.get(1).cursarAsignatura();
//                asignaturasConCorrelativas.get(1).setNota(crearNumeroEntreRangoRandom(0, 5));
//            } else {
//                asignaturasConCorrelativas.get(1).aprobarAsignatura();
//                asignaturasConCorrelativas.get(1).setNota(crearNumeroEntreRangoRandom(6, 10));
//            }
//            int numero2 = crearNumeroEntreRangoRandom(0, 1);
//            if (numero2 == 1) {
//                asignaturasConCorrelativas.get((asignaturasConCorrelativas.size()) - 1).cursarAsignatura();
//                asignaturasConCorrelativas.get((asignaturasConCorrelativas.size()) - 1).setNota(crearNumeroEntreRangoRandom(0, 5));
//            } else {
//                asignaturasConCorrelativas.get((asignaturasConCorrelativas.size()) - 1).aprobarAsignatura();
//                asignaturasConCorrelativas.get((asignaturasConCorrelativas.size()) - 1).setNota(crearNumeroEntreRangoRandom(6, 10));
//            }
            asignaturasConCorrelativas.get(1).cursarAsignatura();
            asignaturasConCorrelativas.get((asignaturasConCorrelativas.size()) - 1).cursarAsignatura();
        }
        return asignaturasConCorrelativas;
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

    public void delAsignaturaByMateriaId(Integer materiaId) throws MateriaNotFoundException {
        if (dao.getAllAsignaturas().size()==0) {
            throw new MateriaNotFoundException("No hay materias.");
        }
        else {
            for (Asignatura asignatura : dao.getAllAsignaturas()) {
                if (asignatura.getMateria().getMateriaId() == materiaId) {
                    dao.del(materiaId);
                }
            }
            throw new MateriaNotFoundException("No se encontro la materia con el id: "+materiaId);
        }
    }
}
