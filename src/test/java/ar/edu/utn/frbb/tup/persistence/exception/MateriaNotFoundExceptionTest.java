package ar.edu.utn.frbb.tup.persistence.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MateriaNotFoundExceptionTest {

    @Test
    void materiaNotFoundExceptionTest() {
        MateriaNotFoundException ex = new MateriaNotFoundException("error");
        assertEquals(ex.getMessage(), "error");
    }
}