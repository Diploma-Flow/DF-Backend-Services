package org.example.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.authservice.data.TokenType;
import org.example.authservice.data.entity.User;
import org.example.authservice.data.enums.UserRole;
import org.example.authservice.request.inbound.LoginRequest;
import org.example.authservice.request.inbound.RegisterRequest;
import org.example.authservice.request.outbound.SaveUserRequest;
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
        SaveUserRequest saveUserRequest = SaveUserRequest
                .builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

//        User savedUser = restTemplate.postForObject("http://user-service/user/save", saveUserRequest, User.class);

        User savedUser = User.builder()
                .email(request.getEmail())
                .role(UserRole.STUDENT)
                .build();

        Map<TokenType, String> jwtTokens = jwtService.generateJwtTokens(savedUser);

        try {
            tokenService.persist(savedUser, jwtTokens);

        } catch (DataIntegrityViolationException e) {
            //log the error
            return AuthenticationResponse.<TokenData>builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .response("Registration failed: " + e.getMessage())
                    .build();
        }

        return AuthenticationResponse.<TokenData>builder()
                .httpStatus(HttpStatus.OK)
                .data(TokenData.builder().accessToken(jwtTokens.get(TokenType.ACCESS)).refreshToken(jwtTokens.get(TokenType.REFRESH)).build())
                .response("Registration successful")
                .build();
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        //TODO check if user exists in user-service
        return null;
    }

    //THIS is made to send response and status 200 OK and 401 UNAUTHORIZED every other status will be considered as error
    @Override
    public JwtValidationResponse validate(String jwtToken) {
        //TODO use jwt do decrypt and check if the token is not expired
        //TODO if token not expired return 200 OK
        return JwtValidationResponse
                .builder().response("SUCCESS").httpStatus(HttpStatus.OK).build();
    }
}
