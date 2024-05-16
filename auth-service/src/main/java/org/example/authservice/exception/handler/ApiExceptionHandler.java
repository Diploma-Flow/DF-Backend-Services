package org.example.authservice.exception.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.example.authservice.exception.exceptions.InvalidLoginCredentialsException;
import org.example.authservice.exception.helper.ApiException;
import org.example.authservice.exception.helper.ApiExceptionFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */

@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler {

    private final ApiExceptionFactory apiExceptionFactory;

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiException apiException = apiExceptionFactory.generateApiException("Registration failed: " + e.getMessage(), httpStatus);

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(InvalidLoginCredentialsException.class)
    public ResponseEntity<Object> handleInvalidLoginCredentialsException(InvalidLoginCredentialsException e) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ApiException apiException = apiExceptionFactory.generateApiException(e, httpStatus);

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Object> handleSignatureException(SignatureException e) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ApiException apiException = apiExceptionFactory.generateApiException(e, httpStatus);

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException e) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ApiException apiException = apiExceptionFactory.generateApiException(e, httpStatus);

        return new ResponseEntity<>(apiException, httpStatus);
    }


}
