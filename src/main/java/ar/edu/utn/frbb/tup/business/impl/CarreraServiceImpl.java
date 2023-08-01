package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.persistence.CarreraDao;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CarreraServiceImpl implements CarreraService {

    @Autowired
    private CarreraDao dao;

    @Override
    public Carrera crearCarrera(CarreraDto carreraDto) throws CarreraServiceException {
        Carrera c = new Carrera();
        checkCarreraDto(carreraDto);
        c.setNombre(carreraDto.getNombre());
        c.setCantidadCuatrimestres((carreraDto.getCantidadAnios()*12)/4);
        c.setCodigoCarrera(carreraDto.getCodigoCarrera());
        c.setMateriasList(new ArrayList<>());
        dao.save(c);

        return c;
    }

    @Override
    public Carrera getCarreraById(Integer idCarrera) throws CarreraNotFoundException {
        return dao.findById(idCarrera);
    }

    @Override
    public Carrera putCarreraById(Integer idCarrera, CarreraDto carreraDto) throws CarreraNotFoundException, CarreraServiceException {
        Carrera c = getCarreraById(idCarrera);

        if (!carreraDto.getNombre().matches(".*[a-zA-Z]+.*")) {
            throw new CarreraServiceException("Falta el nombre de la carrera");
        }
        else if (carreraDto.getCantidadAnios() <= 0) {
            throw new CarreraServiceException("Falta el año de la carrera");
        }

        c.setNombre(carreraDto.getNombre());
        c.setCantidadCuatrimestres((carreraDto.getCantidadAnios()*12)/4);
        return c;
    }

    public Carrera delCarreraById(Integer idCarrera) throws CarreraNotFoundException {
        for (Carrera carrera : getAllCarreras()) {
            if (carrera.getCodigoCarrera()==idCarrera) {
                dao.del(carrera);
                return carrera;
            }
        }
        throw new CarreraNotFoundException("No se encontro la carrera con el id: "+idCarrera);
    }

    public List<Carrera> getAllCarreras() {
        List<Carrera> carrerasList = new ArrayList<>();
        Map<Integer, Carrera> carreras = dao.getAllCarreras();
        for (Carrera carrera : carreras.values()) {
            if (!carrerasList.contains(carrera)) {
                carrerasList.add(carrera);
            }
        }
        return carrerasList;
    }

    public void checkCarreraDto(CarreraDto carreraDto) throws CarreraServiceException {
        if (!carreraDto.getNombre().matches(".*[a-zA-Z]+.*")) {
            throw new CarreraServiceException("Falta el nombre de la carrera");
        }
        else if (carreraDto.getCantidadAnios() <= 0) {
            throw new CarreraServiceException("Falta el año de la carrera");
        }
        else if (carreraDto.getCodigoCarrera() <= 0) {
            throw new CarreraServiceException("Falta el código de la carrera");
        }
    }
}
