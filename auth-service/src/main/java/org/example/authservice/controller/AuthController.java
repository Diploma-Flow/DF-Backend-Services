package org.example.authservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.example.authservice.request.LogoutRequest;
import org.example.authservice.request.RefreshTokenRequest;
import org.example.authservice.request.LoginRequest;
import org.example.authservice.request.RegisterRequest;
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
    public ResponseEntity<AuthenticationResponse<Void>> register(@RequestBody RegisterRequest request) {
        final String methodName = "register";
        log.entering(SOURCE_CLASS, methodName);

        AuthenticationResponse<Void> authServiceResponse = authService.register(request);
        return ResponseEntity.status(authServiceResponse.getHttpStatus()).body(authServiceResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse<TokenData>> login(@RequestBody LoginRequest request) {
        final String methodName = "login";
        log.entering(SOURCE_CLASS, methodName);

        AuthenticationResponse<TokenData> authServiceResponse = authService.login(request);
        return ResponseEntity.status(authServiceResponse.getHttpStatus()).body(authServiceResponse);
    }

//    //TODO
//    @PostMapping("/logout")
//    public ResponseEntity<AuthenticationResponse<Void>> logout(@RequestBody LogoutRequest logoutRequest){
//        final String methodName = "logout";
//        log.entering(SOURCE_CLASS, methodName);
//
//        AuthenticationResponse<Void> authServiceResponse = authService.logout(logoutRequest);
//        return ResponseEntity.status(authServiceResponse.getHttpStatus()).body(authServiceResponse);
//    }

    //TODO
    @PostMapping("/validate")
    public ResponseEntity<AuthenticationResponse<Void>> validate(@RequestBody String jwtToken){
        final String methodName = "validate";
        log.entering(SOURCE_CLASS, methodName);

        AuthenticationResponse<Void> authServiceResponse = authService.validate(jwtToken);
        return ResponseEntity.status(authServiceResponse.getHttpStatus()).body(authServiceResponse);
    }

    //TODO
    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse<TokenData>> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        final String methodName = "refresh";
        log.entering(SOURCE_CLASS, methodName);

        AuthenticationResponse<TokenData> authServiceResponse = authService.refresh(refreshTokenRequest);
        return ResponseEntity.status(authServiceResponse.getHttpStatus()).body(authServiceResponse);
    }
}
