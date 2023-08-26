package ar.edu.utn.frbb.tup.persistence.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlumnoNotFoundExceptionTest {

    @Test
    void alumnoNotFound() {
        AlumnoNotFoundException ex = new AlumnoNotFoundException("error");
        assertEquals(ex.getMessage(), "error");
    }
}