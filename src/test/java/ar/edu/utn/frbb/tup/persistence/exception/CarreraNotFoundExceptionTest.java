package ar.edu.utn.frbb.tup.persistence.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarreraNotFoundExceptionTest {

    @Test
    void carreraNotFoundExceptionTest() {
        CarreraNotFoundException ex = new CarreraNotFoundException("error");
        assertEquals(ex.getMessage(), "error");
    }
}