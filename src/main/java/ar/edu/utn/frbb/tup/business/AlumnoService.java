package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.persistence.exception.*;

public interface AlumnoService {
    Alumno crearAlumno(AlumnoDto alumno) throws AlumnoServiceException, AsignaturaNotFoundException, AlumnoNotFoundException, AsignaturaServiceException;

    Alumno getAlumnoById(int idAlumno) throws AlumnoNotFoundException;

    Alumno putAlumnoById(int idAlumno, AlumnoDto alumnoDto) throws AlumnoNotFoundException, AlumnoServiceException;

    Alumno delAlumnoById(int idAlumno) throws AlumnoNotFoundException;

    void delMateriaAlumnoByMateriaDel(Materia materia);

    void actualizarProfesoresDeLasMateriasDeLosAlumnos() throws ProfesorNotFoundException;

    void actualizarNombreMateriaYSusCorrelativasDeLasMateriasDelAlumno(String nombreMateriaViejo, String nombreMateriaNuevo);

    void actualizarCorrelativasAlumnoByNameMateriaDeleted(String nombreMateriaDeleted);
}
