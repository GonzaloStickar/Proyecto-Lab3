package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;

import java.util.List;

public interface MateriaService {
    Materia crearMateria(MateriaDto inputData) throws IllegalArgumentException;

    List<Materia> getAllMaterias();

    Materia getMateriaById(int materiaId) throws MateriaNotFoundException;

    Materia putMateriaById(int materiaId, MateriaDto materia) throws MateriaNotFoundException;

    Materia delMateriaById(Integer materiaId) throws MateriaNotFoundException;

    List<Materia> getAllMateriasSortedByNameAsc();

    List<Materia> getAllMateriasSortedByNameDesc();

    List<Materia> getAllMateriasSortedByCodAsc();
    List<Materia> getAllMateriasSortedByCodDesc();

    List<Materia> getAllMateriasByName(String nombre);
}
