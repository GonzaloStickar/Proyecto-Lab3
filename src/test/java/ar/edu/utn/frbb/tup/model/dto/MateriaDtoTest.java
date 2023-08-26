package ar.edu.utn.frbb.tup.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MateriaDtoTest {

    @Test
    void getProfesorId() {
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setProfesorId(123);
        assertEquals(123, materiaDto.getProfesorId());
    }

    @Test
    void setProfesorId() {
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setProfesorId(123);
        assertEquals(123, materiaDto.getProfesorId());
    }

    @Test
    void getNombre() {
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("pepe 1");
        assertEquals("pepe 1", materiaDto.getNombre());
    }

    @Test
    void setNombre() {
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setNombre("pepe 1");
        assertEquals("pepe 1", materiaDto.getNombre());
    }

    @Test
    void getAnio() {
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setAnio(1);
        assertEquals(1, materiaDto.getAnio());
    }

    @Test
    void setAnio() {
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setAnio(1);
        assertEquals(1, materiaDto.getAnio());
    }

    @Test
    void getCuatrimestre() {
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setCuatrimestre(1);
        assertEquals(1, materiaDto.getCuatrimestre());
    }

    @Test
    void setCuatrimestre() {
        MateriaDto materiaDto = new MateriaDto();
        materiaDto.setCuatrimestre(1);
        assertEquals(1, materiaDto.getCuatrimestre());
    }
}