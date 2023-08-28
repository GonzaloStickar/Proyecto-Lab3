package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.*;

import java.util.List;

public interface AsignaturaService {

    Asignatura buscarAsignaturaPorNombre(String nombre);

    void crearAsignatura(Materia materia);

    Alumno putAsignatura(int idAlumno, int idAsignatura, AsignaturaDto asignaturaDto) throws AlumnoNotFoundException, AsignaturaNotFoundException, AsignaturaServiceException, AlumnoServiceException, EstadoIncorrectoException, MateriaNotFoundException;

    List<Asignatura> getAllAsignaturas();

    void actualizarAsignaturaByMateria(Materia materia);

    void delAsignaturaByMateria(Materia materia) throws MateriaNotFoundException;

    List<Asignatura> getSomeAsignaturaRandomFromAsignaturasDao() throws AsignaturaNotFoundException;

    void verificador(Asignatura asignatura, List<Asignatura> asignaturasSinDuplicado);

    List<Asignatura> checkAsignaturaCorrelativas(Asignatura asignatura, List<Asignatura> listaAsignaturasExtraCursadasAprobadas);

    Alumno aprobarAsignatura(int idAlumno, int idAsignatura, int nota) throws AlumnoNotFoundException, AsignaturaNotFoundException, AlumnoServiceException, EstadoIncorrectoException, AsignaturaServiceException, MateriaNotFoundException;
}
