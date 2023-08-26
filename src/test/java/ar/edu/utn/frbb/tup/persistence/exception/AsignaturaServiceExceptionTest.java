package ar.edu.utn.frbb.tup.persistence.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class AsignaturaServiceExceptionTest {

    @Test
    void getMessage() {
        AsignaturaServiceException ex = new AsignaturaServiceException("error", HttpStatus.CONFLICT);
        assertEquals(ex.getMessage(), "error");
    }

    @Test
    void getHttpStatus() {
        AsignaturaServiceException ex = new AsignaturaServiceException("error", HttpStatus.CONFLICT);
        assertEquals(ex.getHttpStatus(), HttpStatus.CONFLICT);
    }
}