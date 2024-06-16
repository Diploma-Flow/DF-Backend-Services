package org.example.authservice.exception.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.authservice.exception.exceptions.*;
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

@Log4j2
@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler {

    private final ApiExceptionFactory apiExceptionFactory;

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        log.warn(e.getMessage());
        ApiException apiException = apiExceptionFactory.generateApiException("User already logged in", httpStatus);

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
        ApiException apiException = apiExceptionFactory.generateApiException("TOKEN EXPIRED", httpStatus);

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(UserLoginException.class)
    public ResponseEntity<Object> handleUserLoginException(UserLoginException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiException apiException = apiExceptionFactory.generateApiException(e, httpStatus);

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<Object> handleUserAlreadyRegisteredException(UserAlreadyRegisteredException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiException apiException = apiExceptionFactory.generateApiException(e, httpStatus);

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(UserServiceInternalServerError.class)
    public ResponseEntity<Object> handleUserUserServiceInternalServerError(UserServiceInternalServerError e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiException apiException = apiExceptionFactory.generateApiException(e.getMessage(), httpStatus);

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(UserAlreadyLoggedOutException.class)
    public ResponseEntity<Object> handleUserAlreadyLoggedOutException(UserAlreadyLoggedOutException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiException apiException = apiExceptionFactory.generateApiException(e.getMessage(), httpStatus);

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(InvalidJwtTokenException.class)
    public ResponseEntity<Object> handleInvalidJwtTokenException(InvalidJwtTokenException e) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ApiException apiException = apiExceptionFactory.generateApiException(e.getMessage(), httpStatus);

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiException apiException = apiExceptionFactory.generateApiException(e.getMessage(), httpStatus);

        return new ResponseEntity<>(apiException, httpStatus);
    }

}
