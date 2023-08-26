package ar.edu.utn.frbb.tup.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlumnoDtoTest {

    @Test
    void getNombre() {
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("pepito");
        assertEquals("pepito", alumnoDto.getNombre());
    }

    @Test
    void setNombre() {
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("pepito");
        assertEquals("pepito", alumnoDto.getNombre());
    }

    @Test
    void getApellido() {
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setApellido("pepito");
        assertEquals("pepito", alumnoDto.getApellido());
    }

    @Test
    void setApellido() {
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setApellido("pepito");
        assertEquals("pepito", alumnoDto.getApellido());
    }

    @Test
    void getDni() {
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setDni(1);
        assertEquals(1, alumnoDto.getDni());
    }

    @Test
    void setDni() {
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setDni(1);
        assertEquals(1, alumnoDto.getDni());
    }
}