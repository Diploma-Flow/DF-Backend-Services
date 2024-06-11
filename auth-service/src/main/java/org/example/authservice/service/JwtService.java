package org.example.authservice.service;

import io.jsonwebtoken.Claims;
import org.example.authservice.enums.TokenType;
import org.example.authservice.dto.User;
import org.example.authservice.enums.UserRole;

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

    boolean checkNotExpired(String jsonWebToken);

    String extractEmail(String jsonWebToken);

    TokenType extractTokenType(String jsonWebToken);

    boolean isTokenType(String jsonWebToken, TokenType tokenType);

    UserRole extractUserRole(String jsonWebToken);

    void verifyAccessTokenType(String jwtToken);

    void verifyRefreshTokenType(String jwtToken);

    String getJwtFromHeader(String authHeader);
}
