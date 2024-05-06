package org.example.userservice.exception.handler;

import lombok.RequiredArgsConstructor;
import org.example.userservice.exception.helper.ApiException;
import org.example.userservice.exception.helper.ApiExceptionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */

@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler {

    private final ApiExceptionFactory apiExceptionFactory;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleUserNotRegisteredException(RuntimeException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiException apiException = apiExceptionFactory.generateApiException(e, httpStatus);

        return new ResponseEntity<>(apiException, httpStatus);
    }

}
