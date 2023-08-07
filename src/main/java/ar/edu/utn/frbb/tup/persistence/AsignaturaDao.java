package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;

import java.util.List;

public interface AsignaturaDao {
    List<Asignatura> getAllAsignaturas();

    void save(Materia materia);

    void del(Materia delMateria) throws MateriaNotFoundException;

    Asignatura getAsignaturaById(int idAsignatura) throws AsignaturaNotFoundException;

    Asignatura getAsignaturaByName(String nombreMateria) throws AsignaturaNotFoundException;
}
