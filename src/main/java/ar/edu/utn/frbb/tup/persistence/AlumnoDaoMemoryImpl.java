package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
public class AlumnoDaoMemoryImpl implements AlumnoDao {

    private static Map<Long, Alumno> repositorioAlumnos = new HashMap<>();

    @Override
    public void saveAlumno(Alumno alumno) {
        Random random = new Random();
        for (Alumno a: repositorioAlumnos.values()) {
            System.out.println(a.getApellido());
        }
        alumno.setId(random.nextLong());
        repositorioAlumnos.put(alumno.getDni(), alumno);
    }

    @Override
    public Alumno findAlumno(String apellidoAlumno) {
        for (Alumno a: repositorioAlumnos.values()) {
            System.out.println(a.getApellido());
            if (a.getApellido().equals(apellidoAlumno)){
                return a;
            }
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No existen alumnos con esos datos."
        );
    }

    @Override
    public Alumno loadAlumno(Long dni) {
        return null;
    }

}
