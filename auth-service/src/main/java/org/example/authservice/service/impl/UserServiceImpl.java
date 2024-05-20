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
import org.example.authservice.exception.exceptions.UserServiceConnectionTimeoutException;
import org.example.authservice.exception.exceptions.UserServiceInternalServerError;
import org.example.authservice.service.UserService;
import org.example.authservice.util.ServiceProperties;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Author: Simeon Popov
 * Date of creation: 3.5.2024 г.
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final ModelMapper modelMapper;
    private final RestClient.Builder restClientBuilder;
    private final PasswordEncoder passwordEncoder;
    private final ServiceProperties serviceProperties;

    @Override
    public RegisterResponse registerUser(RegisterRequest request) {

        RegisterUserRequest registerUserRequest = modelMapper.map(request, RegisterUserRequest.class);
        registerUserRequest.setPassword(passwordEncoder.encode(request.getPassword()));
        registerUserRequest.setUserRole(UserRole.GUEST);


        RegisterResponse registerUserResponse = restClientBuilder
                .build()
                .post()
                .uri(serviceProperties.getUserServiceRegisterUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerUserRequest)
                .exchange((req, res)->{
                    RegisterResponse registerResponse = res.bodyTo(RegisterResponse.class);

                    if (registerResponse == null) {
                        throw new UserServiceInternalServerError("NO RESPONSE BODY");
                    }

                    if(res.getStatusCode().is2xxSuccessful()){
                        return registerResponse;
                    }

                    String response = registerResponse.getResponse();

                    if(res.getStatusCode().value() == HttpStatus.BAD_REQUEST.value()){
                        throw new UserAlreadyRegisteredException(response);
                    }

                    throw new UserServiceInternalServerError(response);
                });

        return registerUserResponse;
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {

        LoginResponse loginUserResponse = restClientBuilder
                .build()
                .post()
                .uri(serviceProperties.getUserServiceLoginUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .exchange((req, res)->{
                    LoginResponse loginResponse = res.bodyTo(LoginResponse.class);

                    if (loginResponse == null) {
                        throw new UserServiceInternalServerError("NO RESPONSE BODY");
                    }

                    if(res.getStatusCode().is2xxSuccessful()){
                        return loginResponse;
                    }

                    String response = loginResponse.getResponse();

                    if(res.getStatusCode().value() == HttpStatus.NOT_FOUND.value()){
                        throw new UserNotFoundException(response);
                    }

                    throw new UserServiceInternalServerError(response);
                });

        return loginUserResponse;
    }

    @Override
    public void pingUserService() {
        //If property: user-service.health-check.enabled = true
        //This check will be performed

        ResponseEntity<Void> bodilessEntity = restClientBuilder
                .build()
                .get()
                .uri(serviceProperties.getUserServiceHealthCheckUrl())
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {

                    String httpStatus = res.getStatusText();
                    String message = httpStatus + ": Connection to user service failed";
                    throw new UserServiceConnectionTimeoutException(message);
                })
                .toBodilessEntity();

        //TODO maybe stop the server System.exit(1);
    }
}
