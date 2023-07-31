package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CarreraDaoMemoryImpl implements CarreraDao {
    private static Map<Integer, Carrera> repositorioCarrera = new HashMap<>();

    @Override
    public Carrera findById(int idMateria) throws CarreraNotFoundException {
        return null;
    }

    @Override

    public void save(Carrera carrera) {
        boolean crear=true;
        for (Carrera carreraCodigo : repositorioCarrera.values()) {
            if (carrera.getCodigoCarrera() == carreraCodigo.getCodigoCarrera()) {
                crear = false;
                break;
            }
        }
        if (crear) {
            repositorioCarrera.put((repositorioCarrera.size())+1,carrera);
        }
    }

    public Map<Integer, Carrera> getAllCarreras() {
        return repositorioCarrera;
    }
}
