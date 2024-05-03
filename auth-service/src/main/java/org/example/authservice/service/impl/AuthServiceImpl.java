package org.example.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.authservice.data.TokenType;
import org.example.authservice.data.entity.User;
import org.example.authservice.data.enums.UserRole;
import org.example.authservice.dto.login.LoginRequest;
import org.example.authservice.dto.register.RegisterRequest;
import org.example.authservice.dto.login.LoginResponse;
import org.example.authservice.dto.register.RegisterResponse;
import org.example.authservice.dto.register.RegisterUserRequest;
import org.example.authservice.response.AuthenticationResponse;
import org.example.authservice.response.JwtValidationResponse;
import org.example.authservice.response.TokenData;
import org.example.authservice.service.AuthService;
import org.example.authservice.service.JwtService;
import org.example.authservice.service.TokenService;
import org.example.authservice.service.UserService;
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
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse<TokenData> register(RegisterRequest request) {

        RegisterResponse userRegisterResponse = userService.registerUser(request);

        if(userRegisterResponse.getHttpStatus()
                .is4xxClientError()){
            return buildAuthResponseError(userRegisterResponse.getHttpStatus(), userRegisterResponse.getResponse());
        }

        User user = userRegisterResponse.getUser();

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

        LoginResponse userLoginResponse = userService.loginUser(request);
        if(userLoginResponse.getHttpStatus()
                .is4xxClientError()){
            return buildAuthResponseError(userLoginResponse.getHttpStatus(), userLoginResponse.getResponse());
        }

        User user = userLoginResponse.getUser();

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return buildAuthResponseError(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        // Generate JWT tokens for the retrieved user
        Map<TokenType, String> jwtTokens = jwtService.generateJwtTokens(user);

        try {
            tokenService.updateTokens(user.getEmail(), jwtTokens);

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
