package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;

import java.util.List;

public interface CarreraService {

    Carrera crearCarrera(CarreraDto inputData) throws IllegalArgumentException;

    Carrera getCarreraById(Integer idCarrera);

    Carrera putCarrera(Integer idCarrera,CarreraDto carreraDto);

    Carrera delCarrera(Integer idCarrera);

    List<Carrera> getAllCarreras();
}
