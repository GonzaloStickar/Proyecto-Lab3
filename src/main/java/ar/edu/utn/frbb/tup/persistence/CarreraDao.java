package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;

public interface CarreraDao {

    void save(Carrera c);
    Carrera findById(int idCarrera) throws CarreraNotFoundException;

    void del(Carrera idCarrera);
}
