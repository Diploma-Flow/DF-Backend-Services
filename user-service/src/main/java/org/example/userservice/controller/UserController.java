package org.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.example.userservice.dto.User;
import org.example.userservice.dto.login.LoginRequest;
import org.example.userservice.dto.login.LoginResponse;
import org.example.userservice.dto.register.RegisterUserResponse;
import org.example.userservice.dto.register.RegisterUserRequest;
import org.example.userservice.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Simeon Popov
 * Date of creation: 3.5.2024 г.
 */

@Log
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    public static final String SOURCE_CLASS = UserController.class.getName();
    private final UserService userService;

    @PostMapping("/register")
    public RegisterUserResponse<User> register(@RequestBody RegisterUserRequest registerUserRequest) {
        final String methodName = "register";
        log.entering(SOURCE_CLASS, methodName);

        RegisterUserResponse<User> registerUserResponse = userService.register(registerUserRequest);
        return registerUserResponse;
    }

    @PostMapping("/login")
    public LoginResponse<User> login(@RequestBody LoginRequest loginRequest) {
        final String methodName = "login";
        log.entering(SOURCE_CLASS, methodName);

        LoginResponse<User> loginResponse = userService.loginUser(loginRequest);
        return loginResponse;
    }

}