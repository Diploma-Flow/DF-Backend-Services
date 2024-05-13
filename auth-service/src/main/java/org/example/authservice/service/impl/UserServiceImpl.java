package org.example.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.authservice.data.enums.UserRole;
import org.example.authservice.dto.login.LoginRequest;
import org.example.authservice.dto.login.LoginResponse;
import org.example.authservice.dto.register.RegisterRequest;
import org.example.authservice.dto.register.RegisterResponse;
import org.example.authservice.dto.register.RegisterUserRequest;
import org.example.authservice.exception.exceptions.UserAlreadyRegisteredException;
import org.example.authservice.exception.exceptions.UserNotFoundException;
import org.example.authservice.exception.exceptions.UserServiceInternalServerError;
import org.example.authservice.service.UserService;
import org.example.authservice.util.ResponseValidatorUtil;
import org.example.authservice.util.ServiceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;
    private final ServiceProperties serviceProperties;

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


        RegisterResponse registerResponse = restTemplate.postForObject(serviceProperties.getUSER_SERVICE_REGISTER_URL(), registerUserRequest, RegisterResponse.class);

        return ResponseValidatorUtil
                .of(registerResponse)
                .onStatusThrow(HttpStatus.BAD_REQUEST, UserAlreadyRegisteredException::new)
                .onStatusThrow(HttpStatus.Series.SERVER_ERROR, UserServiceInternalServerError::new)
                .getOnStatus(HttpStatus.OK)
                .orElseThrow(UserServiceInternalServerError::new);
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {

        LoginResponse loginResponse = restTemplate.postForObject(serviceProperties.getUSER_SERVICE_LOGIN_URL(), request, LoginResponse.class);

        return ResponseValidatorUtil
                .of(loginResponse)
                .onStatusThrow(HttpStatus.NOT_FOUND, UserNotFoundException::new)
                .onStatusThrow(HttpStatus.Series.SERVER_ERROR, UserServiceInternalServerError::new)
                .getOnStatus(HttpStatus.OK)
                .orElseThrow(UserServiceInternalServerError::new);
    }

    @Override
    public void pingUserService() {
        try {
            restTemplate.getForObject(serviceProperties.getUSER_SERVICE_HEALTH_CHECK_URL(), String.class);
            System.out.println("user-service is up and running");
            System.out.println("CONNECTED TO USER_SERVICES");

        } catch (Exception e) {
            // If user-service is not available, take appropriate action (e.g., log and prevent auth-service startup)
            System.err.println("user-service is not available: " + e.getMessage());
            System.exit(1); // Exit application or throw exception to prevent startup
        }
    }
}
