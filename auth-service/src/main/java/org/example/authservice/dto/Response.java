package org.example.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.authservice.data.entity.User;
import org.example.authservice.dto.register.RegisterResponse;
import org.example.authservice.exception.exceptions.UserAlreadyRegisteredException;
import org.example.authservice.exception.exceptions.UserServiceInternalServerError;
import org.springframework.http.HttpStatus;

/**
 * Author: Simeon Popov
 * Date of creation: 5/13/2024
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    protected String response;
    protected HttpStatus httpStatus;
    protected User user;
}
