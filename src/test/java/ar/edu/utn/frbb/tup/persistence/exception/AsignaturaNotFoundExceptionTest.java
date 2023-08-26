package ar.edu.utn.frbb.tup.persistence.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AsignaturaNotFoundExceptionTest {

    @Test
    void asignaturaNotFoundExceptionTest() {
        AsignaturaNotFoundException ex = new AsignaturaNotFoundException("error");
        assertEquals(ex.getMessage(), "error");
    }
}