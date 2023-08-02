package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoServiceException;

import java.util.List;

public interface AlumnoService {
    void aprobarAsignatura(int materiaId, int nota, int dni) throws EstadoIncorrectoException, CorrelatividadesNoAprobadasException;

    Alumno crearAlumno(AlumnoDto alumno) throws AlumnoServiceException;

    Alumno getAlumnoById(Integer idAlumno) throws AlumnoNotFoundException;

    Alumno putAlumnoById(Integer idAlumno, AlumnoDto alumnoDto) throws AlumnoNotFoundException, AlumnoServiceException;

    Alumno delAlumnoById(Integer idAlumno) throws AlumnoNotFoundException;

    List<Alumno> getAllAlumnos();
}
