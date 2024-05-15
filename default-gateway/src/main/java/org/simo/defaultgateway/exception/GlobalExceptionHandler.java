package org.simo.defaultgateway.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Simeon Popov
 * Date of creation: 5/16/2024
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HeaderValidationException.class)
    public ResponseEntity<String> handleHeaderValidationException(HeaderValidationException e) {
        return new ResponseEntity<>("HeaderValidationException", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<String> handleJwtValidationException(JwtValidationException e) {
        return new ResponseEntity<>("JwtValidationException", HttpStatus.BAD_REQUEST);
    }
}
