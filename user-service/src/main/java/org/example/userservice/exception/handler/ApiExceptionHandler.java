package org.example.userservice.exception.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.login.LoginResponse;
import org.example.userservice.dto.register.RegisterUserResponse;
import org.example.userservice.exception.exceptions.UserNotFoundException;
import org.example.userservice.exception.helper.ConstraintViolationDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */

@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler {

//    private final ApiExceptionFactory apiExceptionFactory;
//
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<Object> handleUserNotRegisteredException(RuntimeException e) {
//        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
//        ApiException apiException = apiExceptionFactory.generateApiException(e, httpStatus);
//
//        return new ResponseEntity<>(apiException, httpStatus);
//    }

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

    @ExceptionHandler(UserNotFoundException.class)
    public LoginResponse handleUserNotFoundException(UserNotFoundException e) {

        return LoginResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .response("User with such email not found")
                .build();
    }

}
