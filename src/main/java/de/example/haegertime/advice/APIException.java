package de.example.haegertime.advice;

import org.springframework.http.HttpStatus;

public class APIException {
    private final String message;
    private final HttpStatus httpStatus;

    public APIException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
