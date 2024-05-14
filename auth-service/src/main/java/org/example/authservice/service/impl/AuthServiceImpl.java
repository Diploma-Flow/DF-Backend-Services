package org.example.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.authservice.data.Token;
import org.example.authservice.data.TokenType;
import org.example.authservice.data.entity.User;
import org.example.authservice.data.enums.UserRole;
import org.example.authservice.dto.login.LoginRequest;
import org.example.authservice.dto.register.RegisterRequest;
import org.example.authservice.dto.login.LoginResponse;
import org.example.authservice.dto.register.RegisterResponse;
import org.example.authservice.dto.register.RegisterUserRequest;
import org.example.authservice.exception.exceptions.InvalidLoginCredentialsException;
import org.example.authservice.model.UserTokens;
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

import java.util.List;
import java.util.Map;

import static org.example.authservice.util.AuthenticationResponseUtil.buildAuthResponseError;
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
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse<TokenData> register(RegisterRequest request) {

        RegisterResponse userRegisterResponse = userService.registerUser(request);
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
        log.info("Sending login request to user-service");
        LoginResponse userLoginResponse = userService.loginUser(request);
        User user = userLoginResponse.getUser();

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidLoginCredentialsException(); //"Invalid password"
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
    //TODO check if the token is access and not refresh
    @Override
    public JwtValidationResponse validate(String jwtToken) {

        //isExpired throws an error then is expired
        if(jwtService.isExpired(jwtToken)){
            return JwtValidationResponse
                    .builder()
                    .response("EXPIRED")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        String subjectEmail = jwtService.extractEmail(jwtToken);
        UserTokens userTokens = tokenService.getTokens(subjectEmail);

        List<Token> accessTokens = userTokens
                .getAccessTokens();

        if(accessTokens.isEmpty()){
            return JwtValidationResponse
                    .builder()
                    .response("NO ACCESS TOKEN FOUND FOR USER: " + subjectEmail)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        boolean tokenRevoked = accessTokens
                .stream()
                .anyMatch(t -> t
                        .getValue()
                        .equals(jwtToken) && t.isRevoked());

        if(tokenRevoked){
            return JwtValidationResponse
                    .builder()
                    .response("REVOKED TOKEN")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        return JwtValidationResponse
                .builder()
                .response("SUCCESS")
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
