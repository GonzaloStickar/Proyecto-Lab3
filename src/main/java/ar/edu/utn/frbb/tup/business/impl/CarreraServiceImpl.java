package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.persistence.CarreraDao;
import ar.edu.utn.frbb.tup.persistence.CarreraDaoMemoryImpl;
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
    public Carrera crearCarrera(CarreraDto carrera) throws IllegalArgumentException {
        Carrera c = new Carrera();
        c.setNombre(carrera.getNombre());
        c.setCantidadCuatrimestres((carrera.getCantidadAnios()*12)/4);
        c.setDepartamentoInt((getAllCarreras().size())+1); // En Departamento y Código, fijarse
        c.setCodigoCarrera(carrera.getCodigoCarrera());     // que no haya ningún otro con lo mismo...
        c.setMateriasList(new ArrayList<>());
        dao.save(c);

        return c;
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

    public List<Carrera> getAllCarreras() {
        List<Carrera> carrerasList = new ArrayList<>();
        Map<Integer, Carrera> carreras = new CarreraDaoMemoryImpl().getAllCarreras();
        for (Carrera carrera : carreras.values()) {
            if (!carrerasList.contains(carrera)) {
                carrerasList.add(carrera);
            }
        }
        return carrerasList;
    }
}
