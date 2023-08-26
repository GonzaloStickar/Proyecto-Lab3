package ar.edu.utn.frbb.tup.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AsignaturaDtoTest {

    @Test
    void getNota() {
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setNota(8);
        assertEquals(8,asignaturaDto.getNota());
    }

    @Test
    void setNota() {
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setNota(8);
        assertEquals(8,asignaturaDto.getNota());
    }
}