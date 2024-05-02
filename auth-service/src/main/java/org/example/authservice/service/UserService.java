package org.example.authservice.service;

import org.example.authservice.data.entity.User;

/**
 * Author: Simeon Popov
 * Date of creation: 2.5.2024 г.
 */
public interface UserService {
    User saveUser();
    User findUserByEmail(String email);
}
