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

    @ExceptionHandler(value={ItemAlreadyExistsException.class})
    public ResponseEntity<Object> handleItemExistsException(ItemAlreadyExistsException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        APIException apiException = new APIException(e.getMessage(), badRequest);
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value={ListEmptyException.class})
    public ResponseEntity<Object> handleListEmptyException(ListEmptyException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        APIException apiException = new APIException(e.getMessage(), badRequest);
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {InvalidRoleException.class})
    public ResponseEntity<Object> handleInvalidRoleException(InvalidRoleException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        APIException apiException = new APIException(e.getMessage(), badRequest);
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
        public ResponseEntity<APIException> handlEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        APIException apiException = new APIException(e.getMessage(), badRequest);
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<APIException> handleInvalidInputException(InvalidInputException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        APIException apiException = new APIException(e.getMessage(), badRequest);
        return new ResponseEntity<>(apiException, badRequest);
    }
}
