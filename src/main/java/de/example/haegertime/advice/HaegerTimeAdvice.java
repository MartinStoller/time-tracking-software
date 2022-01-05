package de.example.haegertime.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//Globale Behandlung von Ausnahmen
@ControllerAdvice
public class HaegerTimeAdvice {

    @ExceptionHandler(value={ItemNotFoundException.class})
    public ResponseEntity<Object> handleItemNotFoundException(ItemNotFoundException e) {
        HttpStatus notfound = HttpStatus.NOT_FOUND;
        APIException apiException = new APIException(e.getMessage(), notfound);
        return new ResponseEntity<>(apiException, notfound);
    }

}
