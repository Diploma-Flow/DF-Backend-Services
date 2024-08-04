package org.example.userservice.exception.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.login.LoginResponse;
import org.example.userservice.dto.register.RegisterUserResponse;
import org.example.userservice.exception.exceptions.UserNotFoundException;
import org.example.userservice.exception.helper.ApiException;
import org.example.userservice.exception.helper.ApiExceptionFactory;
import org.example.userservice.exception.helper.ConstraintViolationDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */

@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler {

    private final ApiExceptionFactory apiExceptionFactory;

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RegisterUserResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ConstraintViolationDetail> violationDetails = ex.getConstraintViolations()
                .stream()
                .map(violation -> ConstraintViolationDetail.builder()
                        .violationConstraint(violation.getMessage())
                        .providedValue(violation.getInvalidValue().toString())
                        .build())
                .collect(Collectors.toList());

        RegisterUserResponse response = RegisterUserResponse.builder()
                .constraintViolations(violationDetails)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .response("Registration failed")
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler()
    public ResponseEntity<ApiException> handleConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        ApiException apiException = apiExceptionFactory.generateApiException(e, HttpStatus.BAD_REQUEST);
        return ResponseEntity.badRequest().body(apiException);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public LoginResponse handleUserNotFoundException(UserNotFoundException e) {

        return LoginResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .response("User with such email not found")
                .build();
    }

}
