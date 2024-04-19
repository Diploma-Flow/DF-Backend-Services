package org.example.authservice.service;

import org.example.authservice.data.TokenType;
import org.example.authservice.data.entity.User;

import java.util.Map;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */
public interface JwtService {
    String generateToken(User user, TokenType tokenType);

    Map<TokenType, String> generateJwtTokens(User user);
}
