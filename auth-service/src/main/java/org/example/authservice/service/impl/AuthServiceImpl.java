package org.example.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.authservice.request.LoginRequest;
import org.example.authservice.request.RegisterRequest;
import org.example.authservice.response.AuthenticationResponse;
import org.example.authservice.response.JwtValidationResponse;
import org.example.authservice.service.AuthService;
import org.example.authservice.service.JwtService;
import org.example.authservice.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final TokenService tokenService;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        return null;
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        return null;
    }

    //THIS is made to send response and status 200 OK and 401 UNAUTHORIZED every other status will be considered as error
    @Override
    public JwtValidationResponse validate(String jwtToken) {
        return JwtValidationResponse
                .builder().response("SUCCESS").httpStatus(HttpStatus.OK).build();
    }
}
