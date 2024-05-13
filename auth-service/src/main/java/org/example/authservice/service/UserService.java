package org.example.authservice.service;

import org.example.authservice.data.entity.User;
import org.example.authservice.dto.login.LoginRequest;
import org.example.authservice.dto.login.LoginResponse;
import org.example.authservice.dto.register.RegisterRequest;
import org.example.authservice.dto.register.RegisterResponse;
import org.example.authservice.dto.register.RegisterUserRequest;

/**
 * Author: Simeon Popov
 * Date of creation: 2.5.2024 г.
 */
public interface UserService {
    RegisterResponse registerUser(RegisterRequest request);
    LoginResponse loginUser(LoginRequest request);
    void pingUserService();
}
