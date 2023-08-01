package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Profesor;

public interface ProfesorDao {
    Profesor get(long id);
}
