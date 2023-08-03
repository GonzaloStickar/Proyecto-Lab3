package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;

import java.util.Map;

public interface AlumnoDao {

    void saveAlumno(Alumno a);

    //Alumno findAlumnoByApellido(String apellidoAlumno) throws AlumnoNotFoundException;

    Alumno findById(int idAlumno) throws AlumnoNotFoundException;

    Alumno loadAlumnoByDni(Integer dni) throws AlumnoNotFoundException;

    void del(Alumno idAlumno);

    Map<Integer, Alumno> getAllAlumnos();
}
