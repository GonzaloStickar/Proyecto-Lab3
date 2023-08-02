package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.persistence.CarreraDao;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        if (carreraDto.getCodigoCarrera() <= 0) {
            throw new CarreraServiceException("Falta el código de la carrera", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        else {
            c.setNombre(carreraDto.getNombre());
            c.setCantidadCuatrimestres((carreraDto.getCantidadAnios()*12)/4);
            c.setCodigoCarrera(carreraDto.getCodigoCarrera());
            c.setMateriasList(new ArrayList<>());
            for (Carrera carrera : dao.getAllCarreras().values()) {
                if (carrera.getCodigoCarrera() == c.getCodigoCarrera()) {
                    throw new CarreraServiceException("Ya existe una Carrera con el mismo código.", HttpStatus.CONFLICT);
                }
            }
            dao.save(c);
            return c;
        }
    }

    @Override
    public Carrera getCarreraById(Integer idCarrera) throws CarreraNotFoundException {
        if (dao.getAllCarreras().size()==0) {
            throw new CarreraNotFoundException("No hay carreras.");
        }
        return dao.findById(idCarrera);
    }

    @Override
    public Carrera putCarreraById(Integer idCarrera, CarreraDto carreraDto) throws CarreraNotFoundException, CarreraServiceException {
        Carrera c = getCarreraById(idCarrera);
        checkCarreraDto(carreraDto);
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
            throw new CarreraServiceException("Falta el nombre de la carrera",HttpStatus.UNPROCESSABLE_ENTITY);
        }  //Código = 422 - La petición estaba bien formada pero no se pudo seguir debido a errores de semántica.
        else if (carreraDto.getCantidadAnios() <= 0) {
            throw new CarreraServiceException("Falta el año de la carrera",HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
