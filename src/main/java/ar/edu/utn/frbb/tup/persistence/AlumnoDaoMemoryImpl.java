package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.persistence.exception.AlumnoNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AlumnoDaoMemoryImpl implements AlumnoDao {

    private static Map<Integer, Alumno> repositorioAlumnos = new HashMap<>();

    @Override
    public void saveAlumno(Alumno alumno) {
        repositorioAlumnos.put(alumno.getDni(), alumno);
    }

//    @Override
//    public Alumno findAlumnoByApellido(String apellidoAlumno) throws AlumnoNotFoundException {
//        for (Alumno a: repositorioAlumnos.values()) {
//            System.out.println(a.getApellido());
//            if (a.getApellido().equals(apellidoAlumno)){
//                return a;
//            }
//        }
//        throw new AlumnoNotFoundException("No se encontró el alumno con apellido: " + apellidoAlumno);
//    }

    @Override
    public Alumno findById(int idAlumno) throws AlumnoNotFoundException {
        for (Alumno a: repositorioAlumnos.values()) {
            if (a.getId() == idAlumno) {
                return a;
            }
        }
        throw new AlumnoNotFoundException("No se encontró el alumno con id: " + idAlumno);
    }

    @Override
    public Alumno findAlumnoByDni(Integer dni) throws AlumnoNotFoundException {
        for (Alumno a: repositorioAlumnos.values()) {
            System.out.println(a.getApellido());
            if (dni == a.getDni()) {
                return a;
            }
        }
        throw new AlumnoNotFoundException("No existe un alumno con el dni: "+dni);
    }

    public void del(Alumno delAlumno) {
        for (Alumno alumno : repositorioAlumnos.values()) {
            if (alumno.getId() == delAlumno.getId()) {
                repositorioAlumnos.values().remove(delAlumno);
                break;
            }
        }
    }

    public Map<Integer, Alumno> getAllAlumnos() {
        return repositorioAlumnos;
    }
}
