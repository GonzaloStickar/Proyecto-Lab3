package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;

public interface MateriaDao {
    void save(Materia materia);

    Materia findById(int idMateria) throws MateriaNotFoundException;

    void del(Materia delMateria);
}
