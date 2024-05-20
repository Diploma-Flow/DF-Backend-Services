package org.example.authservice.service;

import io.jsonwebtoken.Claims;
import org.example.authservice.data.TokenType;
import org.example.authservice.data.entity.User;

import java.util.Map;
import java.util.function.Function;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */
public interface JwtService {
    String generateToken(User user, TokenType tokenType);

    Map<TokenType, String> generateJwtTokens(User user);

    <T> T extractClaim(String jsonWebToken, Function<Claims, T> claimsResolver);

    boolean notExpired(String jsonWebToken);

    String extractEmail(String jsonWebToken);
}
