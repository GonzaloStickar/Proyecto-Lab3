package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;

public interface CarreraService {

    Carrera postCarrera(CarreraDto carreraDTO);

    Carrera getCarreraById(Integer idCarrera);

    Carrera putCarrera(Integer idCarrera,CarreraDto carreraDto);

    Carrera delCarrera(Integer idCarrera);
}
