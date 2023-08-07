package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoServiceException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaServiceException;

public interface AlumnoService {
    Alumno crearAlumno(AlumnoDto alumno) throws AlumnoServiceException, AsignaturaNotFoundException, AlumnoNotFoundException, AsignaturaServiceException;

    Alumno getAlumnoById(Integer idAlumno) throws AlumnoNotFoundException;

    Alumno putAlumnoById(Integer idAlumno, AlumnoDto alumnoDto) throws AlumnoNotFoundException, AlumnoServiceException;

    Alumno delAlumnoById(Integer idAlumno) throws AlumnoNotFoundException;
}
