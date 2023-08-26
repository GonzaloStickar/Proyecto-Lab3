package ar.edu.utn.frbb.tup.persistence.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class AlumnoServiceExceptionTest {

    @Test
    void getMessage() {
        AlumnoServiceException ex = new AlumnoServiceException("error", HttpStatus.CONFLICT);
        assertEquals(ex.getMessage(), "error");
    }

    @Test
    void getHttpStatus() {
        AlumnoServiceException ex = new AlumnoServiceException("error", HttpStatus.CONFLICT);
        assertEquals(ex.getHttpStatus(), HttpStatus.CONFLICT);
    }
}