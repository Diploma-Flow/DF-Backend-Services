package org.example.authservice.client;

import lombok.RequiredArgsConstructor;
import org.example.authservice.enums.UserRole;
import org.example.authservice.request.LoginRequest;
import org.example.authservice.client.response.UserLoginResponse;
import org.example.authservice.request.RegisterRequest;
import org.example.authservice.client.response.UserRegistrationResponse;
import org.example.authservice.client.request.UserRegistrationRequest;
import org.example.authservice.exception.exceptions.UserAlreadyRegisteredException;
import org.example.authservice.exception.exceptions.UserNotFoundException;
import org.example.authservice.exception.exceptions.UserServiceConnectionTimeoutException;
import org.example.authservice.exception.exceptions.UserServiceInternalServerError;
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
public class UserServiceClient {

    private static final Logger log = LoggerFactory.getLogger(UserServiceClient.class);
    private final ModelMapper modelMapper;
    private final RestClient.Builder restClientBuilder;
    private final PasswordEncoder passwordEncoder;
    private final ServiceProperties serviceProperties;

    public UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest) {

        UserRegistrationResponse registerUserResponse = restClientBuilder
                .build()
                .post()
                .uri(serviceProperties.getUserServiceRegisterUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .body(userRegistrationRequest)
                .exchange((req, res)->{
                    UserRegistrationResponse userRegistrationResponse = res.bodyTo(UserRegistrationResponse.class);

                    if (userRegistrationResponse == null) {
                        throw new UserServiceInternalServerError("NO RESPONSE BODY");
                    }

                    if(res.getStatusCode().is2xxSuccessful()){
                        return userRegistrationResponse;
                    }

                    String response = userRegistrationResponse.getResponse();

                    if(res.getStatusCode().value() == HttpStatus.BAD_REQUEST.value()){
                        if(userRegistrationResponse.getResponse().startsWith("Duplicate entry")){
                            log.warn(userRegistrationResponse.getResponse());
                            throw new UserAlreadyRegisteredException("Email already exists");
                        }
                    }

                    throw new UserServiceInternalServerError(response);
                });

        return registerUserResponse;
    }

    public UserLoginResponse loginUser(LoginRequest request) {

        UserLoginResponse loginUserResponse = restClientBuilder
                .build()
                .post()
                .uri(serviceProperties.getUserServiceLoginUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .exchange((req, res)->{
                    UserLoginResponse userLoginResponse = res.bodyTo(UserLoginResponse.class);

                    if (userLoginResponse == null) {
                        throw new UserServiceInternalServerError("NO RESPONSE BODY");
                    }

                    if(res.getStatusCode().is2xxSuccessful()){
                        return userLoginResponse;
                    }

                    String response = userLoginResponse.getResponse();

                    if(res.getStatusCode().value() == HttpStatus.NOT_FOUND.value()){
                        throw new UserNotFoundException(response);
                    }

                    throw new UserServiceInternalServerError(response);
                });

        return loginUserResponse;
    }

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
                    log.error(message);
                    throw new UserServiceConnectionTimeoutException(message);
                })
                .toBodilessEntity();

        //TODO maybe stop the server System.exit(1);
    }
}
