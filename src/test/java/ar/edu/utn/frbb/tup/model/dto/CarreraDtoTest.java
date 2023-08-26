package ar.edu.utn.frbb.tup.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarreraDtoTest {

    @Test
    void getNombre() {
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("pepe");
        assertEquals("pepe", carreraDto.getNombre());
    }

    @Test
    void setNombre() {
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("pepe");
        assertEquals("pepe", carreraDto.getNombre());
    }

    @Test
    void getCodigoCarrera() {
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setCodigoCarrera(123);
        assertEquals(123, carreraDto.getCodigoCarrera());
    }

    @Test
    void setCodigoCarrera() {
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setCodigoCarrera(123);
        assertEquals(123, carreraDto.getCodigoCarrera());
    }

    @Test
    void getCantidadAnios() {
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setCantidadAnios(1);
        assertEquals(1, carreraDto.getCantidadAnios());
    }

    @Test
    void setCantidadAnios() {
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setCantidadAnios(1);
        assertEquals(1, carreraDto.getCantidadAnios());
    }
}