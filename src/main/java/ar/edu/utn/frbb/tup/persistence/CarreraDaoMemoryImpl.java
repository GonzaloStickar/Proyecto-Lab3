package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CarreraDaoMemoryImpl implements CarreraDao {
    private static final Map<Integer, Carrera> repositorioCarrera = new HashMap<>();

    public static Integer contador = 0;

    @Override

    public void save(Carrera carrera) {
        carrera.setDepartamentoInt(contador);
        contador+=1;
        repositorioCarrera.put(contador,carrera);
    }

    @Override
    public Carrera findById(int idCarerra) throws CarreraNotFoundException {
        for (Carrera c: repositorioCarrera.values()) {
            if (idCarerra == c.getCodigoCarrera()) {
                return c;
            }
        }
        throw new CarreraNotFoundException("No se encontró la carrera con id " + idCarerra);
    }

    public void del(Carrera delCarrera) {
        for (Carrera carrera : repositorioCarrera.values()) {
            if (carrera.getCodigoCarrera() == delCarrera.getCodigoCarrera()) {
                repositorioCarrera.values().remove(delCarrera);
                break;
            }
        }
    }

    public Map<Integer, Carrera> getAllCarreras() {
        return repositorioCarrera;
    }
}
