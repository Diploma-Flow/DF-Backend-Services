package org.example.userservice.service;

import org.example.userservice.dto.User;
import org.example.userservice.dto.login.LoginRequest;
import org.example.userservice.dto.login.LoginResponse;
import org.example.userservice.dto.register.RegisterUserResponse;
import org.example.userservice.dto.register.RegisterUserRequest;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */
public interface UserService {

    RegisterUserResponse register (RegisterUserRequest registerUserRequest);
    LoginResponse loginUser(LoginRequest request);
}
