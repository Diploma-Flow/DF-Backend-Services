package org.example.authservice.service;

import org.example.authservice.request.inbound.LoginRequest;
import org.example.authservice.request.inbound.RegisterRequest;
import org.example.authservice.response.AuthenticationResponse;
import org.example.authservice.response.JwtValidationResponse;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */
public interface AuthService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);

    JwtValidationResponse validate(String jwtToken);
}
