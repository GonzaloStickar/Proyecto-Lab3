package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaServiceException;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraServiceException;

import java.util.List;

public interface CarreraService {

    Carrera crearCarrera(CarreraDto inputData) throws CarreraServiceException, AsignaturaServiceException;

    Carrera getCarreraById(Integer idCarrera) throws CarreraNotFoundException;

    Carrera putCarreraById(Integer idCarrera,CarreraDto carreraDto) throws CarreraNotFoundException, CarreraServiceException;

    Carrera delCarreraById(Integer idCarrera) throws CarreraNotFoundException;

    List<Carrera> getAllCarreras() throws CarreraNotFoundException;

    void checkCarreraDto(CarreraDto carreraDto) throws CarreraServiceException;
}
