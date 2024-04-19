package org.example.authservice.service;

import org.example.authservice.data.TokenType;
import org.example.authservice.data.entity.User;

import java.util.Map;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */
public interface TokenService {
    void persist(User owner, Map<TokenType, String> jwtTokens);
}
