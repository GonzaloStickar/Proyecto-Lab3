package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;

import java.util.List;

public interface ProfesorDao {

    void save(Profesor profesor);

    Profesor get(int id) throws ProfesorNotFoundException;

    List<Profesor> getAllProfesores();
}
