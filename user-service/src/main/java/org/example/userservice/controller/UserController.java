package org.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.example.userservice.dto.User;
import org.example.userservice.dto.UserDto;
import org.example.userservice.dto.login.LoginRequest;
import org.example.userservice.dto.login.LoginResponse;
import org.example.userservice.dto.register.RegisterUserResponse;
import org.example.userservice.dto.register.RegisterUserRequest;
import org.example.userservice.enums.UserRole;
import org.example.userservice.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public RegisterUserResponse register(@RequestBody RegisterUserRequest registerUserRequest) {
        final String methodName = "register";
        log.entering(SOURCE_CLASS, methodName);

        RegisterUserResponse registerUserResponse = userService.register(registerUserRequest);
        return registerUserResponse;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        final String methodName = "login";
        log.entering(SOURCE_CLASS, methodName);

        LoginResponse loginResponse = userService.loginUser(loginRequest);
        return loginResponse;
    }

    @GetMapping("/get-user")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam("userEmail") String userEmail) {
        final String methodName = "getUserByEmail";
        log.entering(SOURCE_CLASS, methodName);

        UserDto userDto = userService.getUserByEmail(userEmail);
        return ResponseEntity.ok().body(userDto);
    }

    @GetMapping("/get-users")
    public ResponseEntity<Page<UserDto>> getUsersByRole(@RequestParam int page, @RequestParam int size, @RequestParam UserRole userRole) {
        final String methodName = "getUsersByRole";
        log.entering(SOURCE_CLASS, methodName);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<UserDto> userDtos = userService.getUsersByRole(userRole, pageRequest);
        return ResponseEntity.ok().body(userDtos);
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        final String methodName = "healthCheck";
        log.entering(SOURCE_CLASS, methodName);

        return "READY";
    }

}
