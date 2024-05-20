package org.example.authservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.example.authservice.dto.RefreshTokenRequest;
import org.example.authservice.dto.login.LoginRequest;
import org.example.authservice.dto.register.RegisterRequest;
import org.example.authservice.response.AuthenticationResponse;
import org.example.authservice.response.JwtValidationResponse;
import org.example.authservice.response.TokenData;
import org.example.authservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */

@Log
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    public static final String SOURCE_CLASS = AuthController.class.getName();
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse<TokenData>> register(@RequestBody RegisterRequest request) {
        final String methodName = "register";
        log.entering(SOURCE_CLASS, methodName);

        AuthenticationResponse<TokenData> authenticationResponse = authService.register(request);
        return ResponseEntity.status(authenticationResponse.getHttpStatus()).body(authenticationResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse<TokenData>> login(@RequestBody LoginRequest request) {
        final String methodName = "login";
        log.entering(SOURCE_CLASS, methodName);

        AuthenticationResponse<TokenData> authenticationResponse = authService.login(request);
        return ResponseEntity.status(authenticationResponse.getHttpStatus()).body(authenticationResponse);
    }

    //TODO
    @PostMapping("/logout")
    public ResponseEntity<JwtValidationResponse> logout(@RequestBody String jwtToken){
        //Validate Jwt
        //Find user
        //delete all Tokens of that user in Mongo
        //return success or bad request
        return null;
    }

    //TODO
    @PostMapping("/validate")
    public ResponseEntity<JwtValidationResponse> validate(@RequestBody String jwtToken){
        final String methodName = "validate";
        log.entering(SOURCE_CLASS, methodName);

        JwtValidationResponse jwtValidationResponse = authService.validate(jwtToken);
        return ResponseEntity.status(jwtValidationResponse.getHttpStatus()).body(jwtValidationResponse);
    }

    //TODO
    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse<TokenData>> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        final String methodName = "refresh";
        log.entering(SOURCE_CLASS, methodName);
        log.info(refreshTokenRequest.toString());

        AuthenticationResponse<TokenData> authenticationResponse = authService.refresh(refreshTokenRequest);
        return ResponseEntity.status(authenticationResponse.getHttpStatus()).body(authenticationResponse);
    }
}
