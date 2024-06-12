package org.example.authservice.service;

import org.example.authservice.request.LogoutRequest;
import org.example.authservice.request.RefreshTokenRequest;
import org.example.authservice.request.LoginRequest;
import org.example.authservice.request.RegisterRequest;
import org.example.authservice.response.AuthenticationResponse;
import org.example.authservice.response.PrincipalDetails;
import org.example.authservice.response.TokenData;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */
public interface AuthService {
    AuthenticationResponse<Void> register(RegisterRequest request);

    AuthenticationResponse<TokenData> login(LoginRequest request);

    AuthenticationResponse<PrincipalDetails> validate(String jwtToken);

    AuthenticationResponse<TokenData> refresh(String authorizationHeader);

    AuthenticationResponse<Void> logout(String authorizationHeader);
}
