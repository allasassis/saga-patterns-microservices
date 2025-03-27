package br.com.microservices.orchestrated.orderservice.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionGlobalHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException exception) {
        return ResponseEntity.badRequest().body(new ExceptionDetails(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }
}
