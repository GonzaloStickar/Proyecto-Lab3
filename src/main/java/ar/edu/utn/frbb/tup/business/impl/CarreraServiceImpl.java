package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.persistence.CarreraDao;
import ar.edu.utn.frbb.tup.persistence.CarreraDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.MateriaDaoMemoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CarreraServiceImpl implements CarreraService {

    private CarreraDao dao;

    public Carrera crearCarrera(CarreraDto carrera) throws IllegalArgumentException{
        Carrera c = new Carrera();
        c.setNombre(carrera.getNombre());
        c.setCantidadCuatrimestres((carrera.getCantidadAnios()*4)/12);
        c.setDepartamentoInt(carrera.getDepartamentoInt()); // En Departamento y Código, fijarse
        c.setCodigoCarrera(carrera.getCodigoCarrera());     // que no haya ningún otro con lo mismo...
        c.setCantidadCuatrimestres(carrera.getCantidadAnios());
        dao.save(c);
        return c;
    }

    @Override
    public Carrera postCarrera(CarreraDto carreraDTO) {
        return null;
    }

    @Override
    public Carrera getCarreraById(Integer idCarrera) {
        return null;
    }

    @Override
    public Carrera putCarrera(Integer idCarrera, CarreraDto carreraDto) {
        return null;
    }

    @Override
    public Carrera delCarrera(Integer idCarrera) {
        return null;
    }
}
