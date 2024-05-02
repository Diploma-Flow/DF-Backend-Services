package org.example.authservice.service;

import org.example.authservice.dto.login.LoginRequest;
import org.example.authservice.dto.register.RegisterRequest;
import org.example.authservice.response.AuthenticationResponse;
import org.example.authservice.response.JwtValidationResponse;
import org.example.authservice.response.TokenData;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */
public interface AuthService {
    AuthenticationResponse<TokenData> register(RegisterRequest request);

    AuthenticationResponse<TokenData> login(LoginRequest request);

    JwtValidationResponse validate(String jwtToken);
}
