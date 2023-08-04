package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;

import java.util.Map;

public interface MateriaDao {
    void save(Materia materia);

    Materia findById(int idMateria) throws MateriaNotFoundException;

    void del(Materia delMateria) throws MateriaNotFoundException;

    Map<Integer, Materia> getAllMaterias();
}
