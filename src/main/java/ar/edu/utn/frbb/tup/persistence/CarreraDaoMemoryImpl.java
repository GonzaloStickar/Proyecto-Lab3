package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class CarreraDaoMemoryImpl implements CarreraDao{
    private static Map<Integer, Carrera> repositorioCarrera = new HashMap<>();

    @Override
    public Carrera findById(int idMateria) throws CarreraNotFoundException {
        return null;
    }

    public void save(Carrera carrera) {
//        Random random = new Random();
//        for (Alumno a: repositorioAlumnos.values()) {
//            System.out.println(a.getApellido());
//        }
//        carrera.setId(random.nextLong());
//        repositorioAlumnos.put(carrera.getDni(), carrera);
        repositorioCarrera.put(carrera.getCodigoCarrera(),carrera);
    }

    public Map<Integer, Carrera> getAllCarreras() {
        return repositorioCarrera;
    }
}
