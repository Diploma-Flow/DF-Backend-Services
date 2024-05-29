package org.example.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.example.authservice.client.UserServiceClient;
import org.example.authservice.client.request.UserRegistrationRequest;
import org.example.authservice.enums.TokenType;
import org.example.authservice.dto.User;
import org.example.authservice.enums.UserRole;
import org.example.authservice.model.UserToken;
import org.example.authservice.request.LoginRequest;
import org.example.authservice.request.LogoutRequest;
import org.example.authservice.request.RefreshTokenRequest;
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
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
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
    private final ModelMapper modelMapper;

    @Override
    public AuthenticationResponse<Void> register(RegisterRequest request) {
        log.info("Sending register request to user-service");

        UserRegistrationRequest userRegistrationRequest = modelMapper.map(request, UserRegistrationRequest.class);
        userRegistrationRequest.setPassword(passwordEncoder.encode(request.getPassword()));
        userRegistrationRequest.setUserRole(UserRole.GUEST);

        UserRegistrationResponse userRegistrationResponse = userServiceClient.registerUser(userRegistrationRequest);

        Type authenticationResponseType = new TypeToken<AuthenticationResponse<Void>>() {}.getType();
        AuthenticationResponse<Void> authenticationResponse = modelMapper.map(userRegistrationResponse, authenticationResponseType);
        authenticationResponse.setTimestamp(ZonedDateTime.now(ZoneId.of("Z")));

        return authenticationResponse;
    }

    @Override
    public AuthenticationResponse<TokenData> login(LoginRequest request) {
        log.info("Sending login request to user-service");

        UserLoginResponse userLoginResponse = userServiceClient.loginUser(request);
        User user = userLoginResponse.getUser(); // 1. Getting user from user-service if found

        // 2. Checking if decoded pass match
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidLoginCredentialsException();
        }

        // 3. Generating access/refresh tokens
        Map<TokenType, String> jwtTokens = jwtService.generateJwtTokens(user);

        // 4. Creating refreshToken object to be saved
        UserToken refreshToken = UserToken.builder()
                .ownerEmail(user.getEmail())
                .type(TokenType.REFRESH)
                .value(jwtTokens.get(TokenType.REFRESH))
                .build();

        // 5. Saving refreshToken
        tokenService.saveToken(refreshToken);

        // 6. If no fail till now everything is okay and return tokens to user
        return buildAuthResponseOk(jwtTokens, "Login successful");
    }

        //TODO validate
    @Override
    public AuthenticationResponse<Void> validate(String jwtToken) {
        log.info("Validating jwt token");

        //checkNotExpired throws an error if expired
        jwtService.checkNotExpired(jwtToken);

        //verifyAccessTokenType throws InvalidJwtTokenException if provided jwtToken NOT access type
        jwtService.verifyAccessTokenType(jwtToken);

        //! Note that here a revocation is NOT needed because the access token has a small-time of expiration

        return AuthenticationResponse.<Void>builder()
                .httpStatus(HttpStatus.OK)
                .response("SUCCESS")
                .timestamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();
    }

    //TODO logout
    @Override
    public AuthenticationResponse<Void> logout(LogoutRequest logoutRequest) {
        log.info("Logging out");

        String providedAccessToken = logoutRequest.getAccessToken();
        jwtService.checkNotExpired(providedAccessToken);
        jwtService.verifyAccessTokenType(providedAccessToken);

        String subjectEmail = jwtService.extractEmail(providedAccessToken);
        tokenService.revokeAllOwnedBy(subjectEmail);

        return AuthenticationResponse.<Void>builder()
                .httpStatus(HttpStatus.OK)
                .response("Logout successful")
                .timestamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();
    }

    //TODO refresh
    @Override
    public AuthenticationResponse<TokenData> refresh(RefreshTokenRequest refreshTokenRequest) {
        log.info("Refreshing jwt tokens");

        String providedRefreshToken = refreshTokenRequest.getRefreshToken();

        jwtService.checkNotExpired(providedRefreshToken);
        jwtService.verifyRefreshTokenType(providedRefreshToken);

        // throws exception if no token found but must change the exception
        UserToken persistedUserToken = tokenService.getTokenByValue(providedRefreshToken);
        tokenService.verifyNotRevoked(persistedUserToken);

        String subjectEmail = jwtService.extractEmail(providedRefreshToken);
        UserRole userRole = jwtService.extractUserRole(providedRefreshToken);

        User user = User.builder()
                .role(userRole)
                .email(subjectEmail)
                .build();

        // 3. Generating access/refresh tokens
        Map<TokenType, String> jwtTokens = jwtService.generateJwtTokens(user);

        // 4. Creating persistedUserToken object to be saved
        UserToken newUserToken = UserToken.builder()
                .ownerEmail(user.getEmail())
                .type(TokenType.REFRESH)
                .value(jwtTokens.get(TokenType.REFRESH))
                .build();

        // 5. Saving new persistedUserToken
        tokenService.saveToken(newUserToken);

        // 6. Revoke the used token
        tokenService.revokeToken(persistedUserToken);

        return buildAuthResponseOk(jwtTokens, "Refreshed successful");
    }
}
