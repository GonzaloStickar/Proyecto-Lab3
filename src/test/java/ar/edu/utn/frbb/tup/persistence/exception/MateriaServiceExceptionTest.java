package ar.edu.utn.frbb.tup.persistence.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class MateriaServiceExceptionTest {

    @Test
    void getMessage() {
        MateriaServiceException ex = new MateriaServiceException("error", HttpStatus.CONFLICT);
        assertEquals(ex.getMessage(), "error");
    }

    @Test
    void getHttpStatus() {
        MateriaServiceException ex = new MateriaServiceException("error", HttpStatus.CONFLICT);
        assertEquals(ex.getHttpStatus(), HttpStatus.CONFLICT);
    }
}