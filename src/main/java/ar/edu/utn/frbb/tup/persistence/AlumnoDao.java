package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;

import java.util.Map;

public interface AlumnoDao {

    void saveAlumno(Alumno a);

    Alumno findAlumno(String apellidoAlumno);

    Alumno findById(int idAlumno) throws AlumnoNotFoundException;

    Alumno loadAlumno(Integer dni);

    void del(Alumno idAlumno);

    Map<Integer, Alumno> getAllAlumnos();
}
