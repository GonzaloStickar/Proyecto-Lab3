package ar.edu.utn.frbb.tup.controller.handler;

import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class UtnResponseEntityExceptionHandlerTest {

    @Test
    void handleMateriaNotFound() {
        UtnResponseEntityExceptionHandler exceptionHandler = new UtnResponseEntityExceptionHandler();
        MateriaNotFoundException ex = new MateriaNotFoundException("No hay materias.");
        ResponseEntity<Object> responseEntity = exceptionHandler.handleMateriaNotFound(ex, null);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void handleCarreraNotFound() {
        UtnResponseEntityExceptionHandler exceptionHandler = new UtnResponseEntityExceptionHandler();
        CarreraNotFoundException ex = new CarreraNotFoundException("No hay carreras.");
        ResponseEntity<Object> responseEntity = exceptionHandler.handleCarreraNotFound(ex, null);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void handleAlumnoNotFound() {
        UtnResponseEntityExceptionHandler exceptionHandler = new UtnResponseEntityExceptionHandler();
        AlumnoNotFoundException ex = new AlumnoNotFoundException("No hay alumnos.");
        ResponseEntity<Object> responseEntity = exceptionHandler.handleAlumnoNotFound(ex, null);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void handleAsignaturaNotFound() {
        UtnResponseEntityExceptionHandler exceptionHandler = new UtnResponseEntityExceptionHandler();
        AsignaturaNotFoundException ex = new AsignaturaNotFoundException("No hay asignaturas.");
        ResponseEntity<Object> responseEntity = exceptionHandler.handleAsignaturaNotFound(ex, null);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void handleArgumentAndStateConflict() {
        UtnResponseEntityExceptionHandler exceptionHandler = new UtnResponseEntityExceptionHandler();
        IllegalArgumentException ex1 = new IllegalArgumentException();
        ResponseEntity<Object> responseEntity1 = exceptionHandler.handleArgumentAndStateConflict(ex1, null);
        assertEquals(HttpStatus.CONFLICT, responseEntity1.getStatusCode());

        IllegalStateException ex2 = new IllegalStateException();
        ResponseEntity<Object> responseEntity2 = exceptionHandler.handleArgumentAndStateConflict(ex2, null);
        assertEquals(HttpStatus.CONFLICT, responseEntity2.getStatusCode());
    }

    @Test
    void handleCarreraService() {
        UtnResponseEntityExceptionHandler exceptionHandler = new UtnResponseEntityExceptionHandler();
        CarreraServiceException ex = new CarreraServiceException("error Conflict", HttpStatus.CONFLICT);
        ResponseEntity<Object> responseEntity1 = exceptionHandler.handleCarreraService(ex, null);
        assertEquals(HttpStatus.CONFLICT, responseEntity1.getStatusCode());
        assertEquals(ex.getMessage(), "error Conflict");
    }

    @Test
    void handleMateriaService() {
        UtnResponseEntityExceptionHandler exceptionHandler = new UtnResponseEntityExceptionHandler();
        MateriaServiceException ex = new MateriaServiceException("error Conflict", HttpStatus.CONFLICT);
        ResponseEntity<Object> responseEntity1 = exceptionHandler.handleMateriaService(ex, null);
        assertEquals(HttpStatus.CONFLICT, responseEntity1.getStatusCode());
        assertEquals(ex.getMessage(), "error Conflict");
    }

    @Test
    void handleAlumnoService() {
        UtnResponseEntityExceptionHandler exceptionHandler = new UtnResponseEntityExceptionHandler();
        AlumnoServiceException ex = new AlumnoServiceException("error Conflict", HttpStatus.CONFLICT);
        ResponseEntity<Object> responseEntity1 = exceptionHandler.handleAlumnoService(ex, null);
        assertEquals(HttpStatus.CONFLICT, responseEntity1.getStatusCode());
        assertEquals(ex.getMessage(), "error Conflict");
    }

    @Test
    void handleAsignaturaService() {
        UtnResponseEntityExceptionHandler exceptionHandler = new UtnResponseEntityExceptionHandler();
        AsignaturaServiceException ex = new AsignaturaServiceException("error Conflict", HttpStatus.CONFLICT);
        ResponseEntity<Object> responseEntity1 = exceptionHandler.handleAsignaturaService(ex, null);
        assertEquals(HttpStatus.CONFLICT, responseEntity1.getStatusCode());
        assertEquals(ex.getMessage(), "error Conflict");
    }

    @Test
    void handleProfesorNotFound() {
        UtnResponseEntityExceptionHandler exceptionHandler = new UtnResponseEntityExceptionHandler();
        ProfesorNotFoundException ex = new ProfesorNotFoundException("No hay profesores con ese id.");
        ResponseEntity<Object> responseEntity = exceptionHandler.handleProfesorNotFound(ex, null);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void handleEstadoIncorrecto() {
        UtnResponseEntityExceptionHandler exceptionHandler = new UtnResponseEntityExceptionHandler();
        EstadoIncorrectoException ex = new EstadoIncorrectoException("error", HttpStatus.NOT_FOUND);
        ResponseEntity<Object> responseEntity1 = exceptionHandler.handleEstadoIncorrecto(ex, null);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity1.getStatusCode());
        assertEquals(ex.getMessage(), "error");
    }

    @Test
    void handleExceptionInternal() {
        UtnResponseEntityExceptionHandler exceptionHandler = new UtnResponseEntityExceptionHandler();
        Exception ex = new Exception("errorMensaje");
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.NOT_FOUND;
        WebRequest request = mock(WebRequest.class);
        ResponseEntity<Object> responseEntity = exceptionHandler.handleExceptionInternal(ex, null, headers, status, request);
        assertTrue(responseEntity.getBody() instanceof CustomApiError);
        CustomApiError errorResponse = (CustomApiError) responseEntity.getBody();
        assertEquals("errorMensaje", errorResponse.getErrorMessage());
    }
}