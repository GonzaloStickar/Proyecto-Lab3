package ar.edu.utn.frbb.tup.persistence.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class CarreraServiceExceptionTest {

    @Test
    void getMessage() {
        CarreraServiceException ex = new CarreraServiceException("error", HttpStatus.CONFLICT);
        assertEquals(ex.getMessage(), "error");
    }

    @Test
    void getHttpStatus() {
        CarreraServiceException ex = new CarreraServiceException("error", HttpStatus.CONFLICT);
        assertEquals(ex.getHttpStatus(), HttpStatus.CONFLICT);
    }
}