package org.example.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.authservice.data.entity.User;
import org.example.authservice.data.enums.UserRole;
import org.example.authservice.dto.login.LoginRequest;
import org.example.authservice.dto.login.LoginResponse;
import org.example.authservice.dto.register.RegisterRequest;
import org.example.authservice.dto.register.RegisterResponse;
import org.example.authservice.dto.register.RegisterUserRequest;
import org.example.authservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Author: Simeon Popov
 * Date of creation: 3.5.2024 г.
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterResponse registerUser(RegisterRequest request) {
        //TODO to use model mapper here
        RegisterUserRequest registerUserRequest = RegisterUserRequest
                .builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .userRole(UserRole.GUEST)
                .build();


        RegisterResponse registerResponse = restTemplate.postForObject("lb://user-service/user/register", registerUserRequest, RegisterResponse.class);
        return registerResponse;
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {

        LoginResponse loginResponse = restTemplate.postForObject("lb://user-service/user/login", request, LoginResponse.class);
        return loginResponse;
    }
}
