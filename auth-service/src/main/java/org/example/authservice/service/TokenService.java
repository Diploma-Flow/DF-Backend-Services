package org.example.authservice.service;

import org.example.authservice.dto.Token;
import org.example.authservice.enums.TokenType;
import org.example.authservice.dto.User;
import org.example.authservice.model.UserTokens;

import java.util.Map;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */
public interface TokenService {
    void persist(User owner, Map<TokenType, String> jwtTokens);
    void updateTokens(String userEmail, Map<TokenType, String> jwtTokens);
    UserTokens getTokens(String userEmail);
    Token findAccessTokenByValueAndEmail(String tokenValue, String subjectEmail);

    Token findRefreshTokenByValueAndEmail(String jwtToken, String subjectEmail);

    void deleteTokensOf(String subjectEmail);
}
