package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaServiceException;
import ar.edu.utn.frbb.tup.persistence.exception.ProfesorNotFoundException;

import java.util.List;

public interface MateriaService {
    Materia crearMateria(MateriaDto inputData) throws MateriaServiceException, ProfesorNotFoundException;

    Materia getMateriaById(int materiaId) throws MateriaNotFoundException;

    Materia putMateriaById(int materiaId, MateriaDto materia) throws MateriaNotFoundException, MateriaServiceException, ProfesorNotFoundException;

    Materia delMateriaById(Integer materiaId) throws MateriaNotFoundException, ProfesorNotFoundException;

    List<Materia> getAllMateriasSortedBy(String order) throws MateriaNotFoundException, MateriaServiceException;

    List<Materia> getAllMateriasSortedByNameAsc() throws MateriaNotFoundException;

    List<Materia> getAllMateriasSortedByNameDesc() throws MateriaNotFoundException;

    List<Materia> getAllMateriasSortedByCodAsc() throws MateriaNotFoundException;
    List<Materia> getAllMateriasSortedByCodDesc() throws MateriaNotFoundException;

    List<Materia> getAllMateriasByName(String nombre) throws MateriaNotFoundException;

    List<Materia> getAllMaterias() throws MateriaNotFoundException;

    void checkMateriaDto(MateriaDto materiaDto) throws MateriaServiceException;
}
