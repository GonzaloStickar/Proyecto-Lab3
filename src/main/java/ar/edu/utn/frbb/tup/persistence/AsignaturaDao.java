package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;

import java.util.List;

public interface AsignaturaDao {
    List<Asignatura> getAllAsignaturas();

    void save(Asignatura asignatura);

    void del(Materia delMateria);

    Asignatura getAsignaturaByName(String nombreMateria) throws AsignaturaNotFoundException;
}
