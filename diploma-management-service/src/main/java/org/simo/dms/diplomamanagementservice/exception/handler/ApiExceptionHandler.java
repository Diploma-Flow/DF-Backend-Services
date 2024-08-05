package org.simo.dms.diplomamanagementservice.exception.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.simo.dms.diplomamanagementservice.exception.exception.UserNotFoundException;
import org.simo.dms.diplomamanagementservice.exception.helper.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */

@Log4j2
@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiException apiException = ApiException
                .builder()
                .httpStatus(httpStatus)
                .response(e.getMessage())
                .timestamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiException apiException = ApiException
                .builder()
                .httpStatus(httpStatus)
                .response(e.getMessage())
                .timestamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();

        return new ResponseEntity<>(apiException, httpStatus);
    }

}
