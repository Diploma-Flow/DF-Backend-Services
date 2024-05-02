package org.example.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.authservice.data.TokenType;
import org.example.authservice.data.entity.User;
import org.example.authservice.data.enums.UserRole;
import org.example.authservice.dto.inbound.login.LoginRequest;
import org.example.authservice.dto.inbound.register.RegisterRequest;
import org.example.authservice.dto.outbound.login.LoginResponse;
import org.example.authservice.dto.outbound.register.RegisterResponse;
import org.example.authservice.response.AuthenticationResponse;
import org.example.authservice.response.JwtValidationResponse;
import org.example.authservice.response.TokenData;
import org.example.authservice.service.AuthService;
import org.example.authservice.service.JwtService;
import org.example.authservice.service.TokenService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.example.authservice.util.AuthenticationResponseUtil.buildAuthResponseError;
import static org.example.authservice.util.AuthenticationResponseUtil.buildAuthResponseOk;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final TokenService tokenService;
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse<TokenData> register(RegisterRequest request) {
        //TODO to use model mapper here
        RegisterRequest registerUserRequest = RegisterRequest
                .builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();


//        User savedUser = restTemplate.postForObject("http://user-service/user/register", saveUserRequest, User.class);

        RegisterResponse<User> registerResponse = RegisterResponse
                .<User>builder()
                .httpStatus(HttpStatus.OK)
                .response("Saved successfully")
                .data(new User(request.getEmail(), UserRole.STUDENT, ""))
                .build();

        User user = registerResponse.getData();

        Map<TokenType, String> jwtTokens = jwtService.generateJwtTokens(user);

        try {
            tokenService.persist(user, jwtTokens);

        } catch (DataIntegrityViolationException e) {
            //log the error
            return buildAuthResponseError(HttpStatus.BAD_REQUEST, "Registration failed: " + e.getMessage());
        }

        return buildAuthResponseOk(jwtTokens, "Registration successful");
    }

    @Override
    public AuthenticationResponse<TokenData> login(LoginRequest request) {

        //SEND login request
        //THEN:
        LoginResponse<User> loginResponse = LoginResponse
                .<User>builder()
                .httpStatus(HttpStatus.OK)
                .response("Found successfully")
                .data(new User(request.getEmail(), UserRole.STUDENT, passwordEncoder.encode(request.getPassword())))
                .build();


        User user = loginResponse.getData();

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return buildAuthResponseError(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        // Generate JWT tokens for the retrieved user
        Map<TokenType, String> jwtTokens = jwtService.generateJwtTokens(user);

        try {
            tokenService.persist(user, jwtTokens);

        } catch (DataIntegrityViolationException e) {
            //log the error
            return buildAuthResponseError(HttpStatus.BAD_REQUEST, "Login failed: " + e.getMessage());
        }

        return buildAuthResponseOk(jwtTokens, "Login successful");
    }

    //THIS is made to send response and status 200 OK and 401 UNAUTHORIZED every other status will be considered as error
    @Override
    public JwtValidationResponse validate(String jwtToken) {
        //TODO use jwt do decrypt and check if the token is not expired
        //TODO if token not expired return 200 OK
        return JwtValidationResponse
                .builder()
                .response("SUCCESS")
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
