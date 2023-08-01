package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaServiceException;

import java.util.List;

public interface MateriaService {
    Materia crearMateria(MateriaDto inputData) throws MateriaServiceException;

    Materia getMateriaById(int materiaId) throws MateriaNotFoundException;

    Materia putMateriaById(int materiaId, MateriaDto materia) throws MateriaNotFoundException, MateriaServiceException;

    Materia delMateriaById(Integer materiaId) throws MateriaNotFoundException;

    List<Materia> getAllMateriasSortedBy(String order) throws MateriaNotFoundException, MateriaServiceException;

    List<Materia> getAllMateriasSortedByNameAsc();

    List<Materia> getAllMateriasSortedByNameDesc();

    List<Materia> getAllMateriasSortedByCodAsc();
    List<Materia> getAllMateriasSortedByCodDesc();

    List<Materia> getAllMateriasByName(String nombre) throws MateriaNotFoundException;

    List<Materia> getAllMaterias();

    void checkMateriaDto(MateriaDto materiaDto) throws MateriaServiceException;
}
