package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoServiceException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaServiceException;

import java.util.List;

public interface AsignaturaService {
    Asignatura getAsignatura(int materiaId) throws AsignaturaNotFoundException;

    Alumno putAsignatura(int idAlumno, int idAsignatura, AsignaturaDto asignaturaDto) throws AlumnoNotFoundException, AsignaturaNotFoundException, AsignaturaServiceException, CorrelatividadesNoAprobadasException, AlumnoServiceException, EstadoIncorrectoException;

    List<Asignatura> getSomeAsignaturasRandomFromAsignaturasDao() throws AsignaturaNotFoundException;

    Alumno aprobarAsignatura(int idAlumno,int idAsignatura, int nota) throws AlumnoNotFoundException, AsignaturaNotFoundException, AlumnoServiceException, EstadoIncorrectoException, AsignaturaServiceException;
}
