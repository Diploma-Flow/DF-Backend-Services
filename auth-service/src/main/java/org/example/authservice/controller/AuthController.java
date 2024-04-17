package org.example.authservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.example.authservice.request.LoginRequest;
import org.example.authservice.request.RegisterRequest;
import org.example.authservice.response.AuthenticationResponse;
import org.example.authservice.response.ValidationResponse;
import org.example.authservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        final String methodName = "register";
        log.entering(SOURCE_CLASS, methodName);

        AuthenticationResponse authenticationResponse = authService.register(request);
        //TODO make the register to return Response entity
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        final String methodName = "login";
        log.entering(SOURCE_CLASS, methodName);

        AuthenticationResponse authenticationResponse = authService.login(request);
        //TODO make the login to return Response entity

        return null;
    }

    //TODO this should be POST but for testing purposes it is GET
    @GetMapping("/validate")
    public ResponseEntity<ValidationResponse> validate(@RequestAttribute("jwtToken") String jwtToken){
        final String methodName = "validate";
        log.entering(SOURCE_CLASS, methodName);

        ValidationResponse validationResponse = authService.validate(jwtToken);
        //TODO make the validate to return Response entity

        return ResponseEntity.status(HttpStatus.OK).body(validationResponse);
    }
}
