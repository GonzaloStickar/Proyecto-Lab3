package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoServiceException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    @Autowired
    private AlumnoDao dao;

    @Autowired
    private AsignaturaService asignaturaService;

    @Override
    public void aprobarAsignatura(int materiaId, int nota, int dni) throws EstadoIncorrectoException, CorrelatividadesNoAprobadasException, AlumnoNotFoundException {
        Alumno alumno = dao.loadAlumnoByDni(dni);
        Asignatura a = asignaturaService.getAsignatura(materiaId);
        for (Materia m: a.getMateria().getCorrelatividades()) {
            Asignatura correlativa = asignaturaService.getAsignatura(m.getMateriaId());
            if (!EstadoAsignatura.APROBADA.equals(correlativa.getEstado())) {
                throw new CorrelatividadesNoAprobadasException("La materia " + m.getNombre() + " debe estar aprobada para aprobar " + a.getNombreAsignatura());
            }
        }
        a.aprobarAsignatura(nota);
        asignaturaService.actualizarAsignatura(a);

        alumno.actualizarAsignatura(a);
        dao.saveAlumno(alumno);
    }

    public Alumno crearAlumno(AlumnoDto alumnoDto) throws AlumnoServiceException, AsignaturaNotFoundException {
        Alumno a = new Alumno();
        checkAlumnoDto(alumnoDto);
        if (alumnoDto.getDni() <= 0) {
            throw new AlumnoServiceException("Falta el dni del alumno",HttpStatus.UNPROCESSABLE_ENTITY);
        }
        else {
            a.setNombre(alumnoDto.getNombre());
            a.setApellido(alumnoDto.getApellido());
            a.setDni(alumnoDto.getDni());
            Random random = new Random();
            a.setId(random.nextInt());
            for (Alumno alumno : dao.getAllAlumnos().values()) {
                if (alumno.getDni() == a.getDni()) {
                    throw new AlumnoServiceException("Ya existe un Alumno con el mismo dni.", HttpStatus.CONFLICT);
                }
            }
            a.setAsignaturas(asignaturaService.getSomeAsignaturasRandomFromAsignaturasDao());
            dao.saveAlumno(a);
            return a;
        }
    }

    public Alumno getAlumnoById(Integer idAlumno) throws AlumnoNotFoundException {
        if (dao.getAllAlumnos().size()==0) {
            throw new AlumnoNotFoundException("No hay alumnos.");
        }
        return dao.findById(idAlumno);
    }

    public Alumno putAlumnoById(Integer idAlumno, AlumnoDto alumnoDto) throws AlumnoNotFoundException, AlumnoServiceException {
        Alumno a = getAlumnoById(idAlumno);
        checkAlumnoDto(alumnoDto);
        a.setNombre(alumnoDto.getNombre());
        a.setApellido(alumnoDto.getApellido());
        return a;
    }

    public void checkAlumnoDto(AlumnoDto alumnoDto) throws AlumnoServiceException {
        if (!alumnoDto.getNombre().matches(".*[a-zA-Z]+.*")) {
            throw new AlumnoServiceException("Falta el nombre del alumno",HttpStatus.UNPROCESSABLE_ENTITY);
        }
        else if (!alumnoDto.getApellido().matches(".*[a-zA-Z]+.*")) {
            throw new AlumnoServiceException("Falta el apellido del alumno",HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public List<Alumno> getAllAlumnos() {
        List<Alumno> alumnosList = new ArrayList<>();
        Map<Integer, Alumno> alumnos = dao.getAllAlumnos();
        for (Alumno alumno : alumnos.values()) {
            if (!alumnosList.contains(alumno)) {
                alumnosList.add(alumno);
            }
        }
        return alumnosList;
    }

    public Alumno delAlumnoById(Integer idAlumno) throws AlumnoNotFoundException {
        for (Alumno alumno : getAllAlumnos()) {
            if (alumno.getId()==idAlumno) {
                dao.del(alumno);
                return alumno;
            }
        }
        throw new AlumnoNotFoundException("No se encontró el alumno con id " + idAlumno);
    }
}
