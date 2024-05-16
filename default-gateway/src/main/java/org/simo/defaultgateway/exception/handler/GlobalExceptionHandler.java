package org.simo.defaultgateway.exception.handler;

import lombok.RequiredArgsConstructor;
import org.simo.defaultgateway.exception.exceptions.HeaderValidationException;
import org.simo.defaultgateway.exception.exceptions.JwtValidationException;
import org.simo.defaultgateway.exception.util.ApiException;
import org.simo.defaultgateway.exception.util.ApiExceptionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Author: Simeon Popov
 * Date of creation: 5/16/2024
 */

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiExceptionFactory apiExceptionFactory;

    @ExceptionHandler(HeaderValidationException.class)
    public ResponseEntity<Object> handleHeaderValidationException(HeaderValidationException e) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ApiException apiException = apiExceptionFactory.generateApiException(e, httpStatus);

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<Object> handleJwtValidationException(JwtValidationException e) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ApiException apiException = apiExceptionFactory.generateApiException(e, httpStatus);

        return new ResponseEntity<>(apiException, httpStatus);
    }
}
