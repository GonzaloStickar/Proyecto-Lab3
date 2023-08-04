package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.*;

import java.util.List;

public interface AsignaturaService {
    Asignatura getAsignatura(int materiaId) throws AsignaturaNotFoundException;

    void crearAsignatura(Materia materia);

    Alumno putAsignatura(int idAlumno, int idAsignatura, AsignaturaDto asignaturaDto) throws AlumnoNotFoundException, AsignaturaNotFoundException, AsignaturaServiceException, CorrelatividadesNoAprobadasException, AlumnoServiceException, EstadoIncorrectoException;

    List<Asignatura> getAllAsignaturas();

    void actualizarAsignaturaByMateria(Materia materia);

    Asignatura delAsignaturaByMateriaId(Integer materiaId) throws MateriaNotFoundException;

    List<Asignatura> getSomeAsignaturasRandomFromAsignaturasDao() throws AsignaturaNotFoundException;

    Alumno aprobarAsignatura(int idAlumno,int idAsignatura, int nota) throws AlumnoNotFoundException, AsignaturaNotFoundException, AlumnoServiceException, EstadoIncorrectoException, AsignaturaServiceException;
}
