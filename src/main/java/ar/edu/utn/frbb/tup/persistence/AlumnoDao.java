package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;

public interface AlumnoDao {

    void saveAlumno(Alumno a);

    Alumno findAlumno(String apellidoAlumno);

    Alumno loadAlumno(Long dni);
}
