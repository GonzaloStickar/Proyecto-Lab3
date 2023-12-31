package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.persistence.exception.*;

import java.util.List;

public interface CarreraService {

    Carrera crearCarrera(CarreraDto inputData) throws CarreraServiceException, AsignaturaServiceException, AsignaturaNotFoundException;

    Carrera getCarreraById(int idCarrera) throws CarreraNotFoundException;

    Carrera putCarreraById(int idCarrera,CarreraDto carreraDto) throws CarreraNotFoundException, CarreraServiceException;

    Carrera delCarreraById(int idCarrera) throws CarreraNotFoundException;

    List<Carrera> getAllCarreras() throws CarreraNotFoundException;

    void checkCarreraDto(CarreraDto carreraDto) throws CarreraServiceException;

    void actualizarProfesoresDeLasCarreras() throws ProfesorNotFoundException;

    void actualizarNombreMateriaEnMateriaListDeCarreraYSusCorrelativas(String nombreMateriaViejo, String nombreMateriaNuevo);

    void delMateriaEnCarreraByMateria(Materia materia);

    void actualizarCorrelativasCarreraByNameMateriaDeleted(String nombreMateriaDeleted);
}
