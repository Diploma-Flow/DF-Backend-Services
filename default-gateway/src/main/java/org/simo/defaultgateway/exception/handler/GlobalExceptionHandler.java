package org.simo.defaultgateway.exception.handler;

import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.simo.defaultgateway.exception.exceptions.HeaderValidationException;
import org.simo.defaultgateway.exception.exceptions.JwtValidationException;
import org.simo.defaultgateway.exception.util.ApiException;
import org.simo.defaultgateway.exception.util.ApiExceptionFactory;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Author: Simeon Popov
 * Date of creation: 5/16/2024
 */

@Log4j2
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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        HttpStatus httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
        ApiException apiException = apiExceptionFactory.generateApiException(e, httpStatus);

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleJwtValidationException(RuntimeException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error(e.getMessage(), e);
        ApiException apiException = apiExceptionFactory.generateApiException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), httpStatus);

        return new ResponseEntity<>(apiException, httpStatus);
    }
}
