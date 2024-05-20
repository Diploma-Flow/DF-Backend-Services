package org.example.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.authservice.client.UserServiceClient;
import org.example.authservice.data.Token;
import org.example.authservice.data.TokenType;
import org.example.authservice.data.entity.User;
import org.example.authservice.data.enums.UserRole;
import org.example.authservice.request.LogoutRequest;
import org.example.authservice.request.RefreshTokenRequest;
import org.example.authservice.request.LoginRequest;
import org.example.authservice.request.RegisterRequest;
import org.example.authservice.response.LoginResponse;
import org.example.authservice.client.response.UserRegistrationResponse;
import org.example.authservice.exception.exceptions.InvalidJwtTokenException;
import org.example.authservice.exception.exceptions.InvalidLoginCredentialsException;
import org.example.authservice.response.AuthenticationResponse;
import org.example.authservice.response.JwtValidationResponse;
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
        LoginResponse userLoginResponse = userServiceClient.loginUser(request);
        User user = userLoginResponse.getUser();

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidLoginCredentialsException(); //"Invalid password"
        }

        // Generate JWT tokens for the retrieved user
        Map<TokenType, String> jwtTokens = jwtService.generateJwtTokens(user);
        tokenService.persist(user, jwtTokens);

        return buildAuthResponseOk(jwtTokens, "Login successful");
    }

    @Override
    public AuthenticationResponse<Void> logout(LogoutRequest logoutRequest) {

        String jwtAccessToken = logoutRequest.getAccessToken();
        //Validate Jwt
        jwtService.notExpired(jwtAccessToken);
        String subjectEmail = jwtService.extractEmail(jwtAccessToken);
        tokenService.deleteTokensOf(subjectEmail);

        return AuthenticationResponse.<Void>builder()
                .httpStatus(HttpStatus.OK)
                .response("Logout successful")
                .timestamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();
    }

    //THIS is made to send response and status 200 OK and 401 UNAUTHORIZED every other status will be considered as error
    //TODO check if the token is access and not refresh
    @Override
    public JwtValidationResponse validate(String jwtToken) {

        //notExpired throws an error if expired
        jwtService.notExpired(jwtToken);
        String subjectEmail = jwtService.extractEmail(jwtToken);
        Token accessToken = tokenService.findAccessTokenByValueAndEmail(jwtToken, subjectEmail);

        if (accessToken.isRevoked()) {
            throw new InvalidJwtTokenException("Token is revoked.");
        }

        return JwtValidationResponse
                .builder()
                .response("SUCCESS")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public AuthenticationResponse<TokenData> refresh(RefreshTokenRequest refreshTokenRequest) {
        String jwtToken = refreshTokenRequest.getRefreshToken();

        //validate refresh token
        jwtService.notExpired(jwtToken);
        String subjectEmail = jwtService.extractEmail(jwtToken);
        Token refreshToken = tokenService.findRefreshTokenByValueAndEmail(jwtToken, subjectEmail);

        if (refreshToken.isRevoked()) {
            throw new InvalidJwtTokenException("Token is revoked.");
        }

        //Token is valid!

        //generate new tokens
        User user = User.builder()
                .role(UserRole.GUEST)
                .email(subjectEmail)
                .build();

        Map<TokenType, String> jwtTokens = jwtService.generateJwtTokens(user);

        //persist new tokens
        tokenService.updateTokens(subjectEmail, jwtTokens);

        //return new tokens
        return buildAuthResponseOk(jwtTokens, "Refreshed successful");
    }
}
