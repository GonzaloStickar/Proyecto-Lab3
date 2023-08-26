package ar.edu.utn.frbb.tup.persistence.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfesorNotFoundExceptionTest {

    @Test
    void profesorNotFoundExceptionTest() {
        ProfesorNotFoundException ex = new ProfesorNotFoundException("error");
        assertEquals(ex.getMessage(), "error");
    }
}