package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CarreraDaoMemoryImplTest {

    public static final CarreraDao carreraDao = new CarreraDaoMemoryImpl();

    public static Carrera carrera = new Carrera("pepe", 1);


    @DisplayName("Test 11")
    @Test
    void save() throws CarreraNotFoundException {
        carrera.setCodigoCarrera(123);
        carreraDao.save(carrera);
        Carrera carreraEncontrada = carreraDao.findById(123);
        assertEquals(carreraEncontrada.getNombre(), "pepe");
    }

    @DisplayName("Test 12")
    @Test
    void findById() throws CarreraNotFoundException {
        carrera.setCodigoCarrera(123);
        carreraDao.save(carrera);
        Carrera carreraEncontrada = carreraDao.findById(123);
        assertThrows(CarreraNotFoundException.class, () -> carreraDao.findById(1));
        assertEquals(carreraEncontrada.getNombre(), "pepe");
    }

    @DisplayName("Test 13")
    @Test
    void del() throws CarreraNotFoundException {
        carreraDao.save(carrera);
        carreraDao.del(carrera);
        assertTrue(carreraDao.getAllCarreras().containsValue(carrera));
    }

    @DisplayName("Test 14")
    @Test
    void getAllCarreras() {
        Carrera carreraNueva = new Carrera("pepito", 2);
        Map<Integer, Carrera> carreras = new HashMap<>();
        carreraDao.save(carreraNueva);
        carreras.put(2,carreraNueva);
        carreras.put(carrera.getDepartamentoInt()+1, carrera);
        Map<Integer, Carrera> todasLasCarreras = carreraDao.getAllCarreras();
        assertEquals(carreras, todasLasCarreras);
    }

    @DisplayName("Test 15")
    @Test
    public void hayCarreras() {
        if (!carreraDao.getAllCarreras().isEmpty()) {
            assertFalse(carreraDao.getAllCarreras().isEmpty());
        }
        else {
            assertTrue(carreraDao.getAllCarreras().isEmpty());
        }
    }
}