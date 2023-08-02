package ar.edu.utn.frbb.tup.persistence.exception;

import org.springframework.http.HttpStatus;

public class AlumnoServiceException extends Exception {

    private final String message;
    private final HttpStatus httpStatus;
    public AlumnoServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.message=message;
        this.httpStatus=httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
