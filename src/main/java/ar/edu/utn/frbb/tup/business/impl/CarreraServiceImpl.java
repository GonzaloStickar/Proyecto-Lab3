package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.persistence.CarreraDao;
import ar.edu.utn.frbb.tup.persistence.CarreraDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
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
    public Carrera getCarreraById(Integer idCarrera) throws CarreraNotFoundException {
        return dao.findById(idCarrera);
    }

    @Override
    public Carrera putCarreraById(Integer idCarrera, CarreraDto carrera) throws CarreraNotFoundException {
        Carrera c = getCarreraById(idCarrera);
        c.setNombre(carrera.getNombre());
        c.setCantidadCuatrimestres((carrera.getCantidadAnios()*12)/4);
        c.setDepartamentoInt((getAllCarreras().size())+1); // En Departamento y Código, fijarse
        c.setCodigoCarrera(carrera.getCodigoCarrera());     // que no haya ningún otro con lo mismo...
        c.setMateriasList(new ArrayList<>());
        return c;
    }

    public void delCarreraById(Integer idCarrera) throws CarreraNotFoundException {
        List<Carrera> carreraListList = getAllCarreras();
        for (Carrera carrera : carreraListList) {
            if (carrera.getCodigoCarrera()==idCarrera) {
                dao.del(carrera);
                break;
            }
        }
    }

    public boolean checkCarreraId(Integer idCarrera) {
        for (Carrera carrera : getAllCarreras()) {
            if (carrera.getCodigoCarrera()==idCarrera) {
                return true;
            }
        }
        return false;
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
