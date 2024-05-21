package org.example.authservice.service.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.authservice.client.UserServiceClient;
import org.example.authservice.dto.Token;
import org.example.authservice.enums.TokenType;
import org.example.authservice.dto.User;
import org.example.authservice.enums.UserRole;
import org.example.authservice.request.LogoutRequest;
import org.example.authservice.request.RefreshTokenRequest;
import org.example.authservice.request.LoginRequest;
import org.example.authservice.request.RegisterRequest;
import org.example.authservice.client.response.UserLoginResponse;
import org.example.authservice.client.response.UserRegistrationResponse;
import org.example.authservice.exception.exceptions.InvalidJwtTokenException;
import org.example.authservice.exception.exceptions.InvalidLoginCredentialsException;
import org.example.authservice.response.AuthenticationResponse;
import org.example.authservice.response.TokenData;
import org.example.authservice.service.AuthService;
import org.example.authservice.service.JwtService;
import org.example.authservice.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

import static org.example.authservice.util.AuthenticationResponseUtil.buildAuthResponseOk;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */

@Log4j2
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final TokenService tokenService;
    private final UserServiceClient userServiceClient;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse<Void> register(RegisterRequest request) {
        log.info("Sending register request to user-service");

        UserRegistrationResponse userRegistrationResponse = userServiceClient.registerUser(request);

        return AuthenticationResponse.<Void>builder()
                .httpStatus(userRegistrationResponse.getHttpStatus())
                .response(userRegistrationResponse.getResponse())
                .timestamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();
    }

    @Override
    public AuthenticationResponse<TokenData> login(LoginRequest request) {
        log.info("Sending login request to user-service");

        UserLoginResponse userLoginResponse = userServiceClient.loginUser(request);
        User user = userLoginResponse.getUser();

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidLoginCredentialsException();
        }

        Map<TokenType, String> jwtTokens = jwtService.generateJwtTokens(user);
        tokenService.persist(user, jwtTokens);

        return buildAuthResponseOk(jwtTokens, "Login successful");
    }

    @Override
    public AuthenticationResponse<Void> logout(LogoutRequest logoutRequest) {
        log.info("Logging out");

        String jwtAccessToken = logoutRequest.getAccessToken();
        jwtService.notExpired(jwtAccessToken);
        String subjectEmail = jwtService.extractEmail(jwtAccessToken);
        tokenService.deleteTokensOf(subjectEmail);

        return AuthenticationResponse.<Void>builder()
                .httpStatus(HttpStatus.OK)
                .response("Logout successful")
                .timestamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();
    }

    @Override
    public AuthenticationResponse<Void> validate(String jwtToken) {
        log.info("Validating jwt token");

        //notExpired throws an error if expired
        jwtService.notExpired(jwtToken);
        String subjectEmail = jwtService.extractEmail(jwtToken);
        Token accessToken = tokenService.findAccessTokenByValueAndEmail(jwtToken, subjectEmail);

        if (accessToken.isRevoked()) {
            throw new InvalidJwtTokenException("Token is revoked.");
        }

        return AuthenticationResponse.<Void>builder()
                .httpStatus(HttpStatus.OK)
                .response("SUCCESS")
                .timestamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();
    }

    @Override
    public AuthenticationResponse<TokenData> refresh(RefreshTokenRequest refreshTokenRequest) {
        log.info("Refreshing jwt tokens");

        String jwtToken = refreshTokenRequest.getRefreshToken();
        jwtService.notExpired(jwtToken);
        String subjectEmail = jwtService.extractEmail(jwtToken);
        UserRole userRole = jwtService.extractUserRole(jwtToken);
        Token refreshToken = tokenService.findRefreshTokenByValueAndEmail(jwtToken, subjectEmail);

        if (refreshToken.isRevoked()) {
            throw new InvalidJwtTokenException("Token is revoked.");
        }

        User user = User.builder()
                .role(userRole)
                .email(subjectEmail)
                .build();

        Map<TokenType, String> jwtTokens = jwtService.generateJwtTokens(user);
        tokenService.updateTokens(subjectEmail, jwtTokens);

        return buildAuthResponseOk(jwtTokens, "Refreshed successful");
    }
}
