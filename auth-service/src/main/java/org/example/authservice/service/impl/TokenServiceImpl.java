package org.example.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.authservice.data.Token;
import org.example.authservice.data.TokenType;
import org.example.authservice.data.entity.User;
import org.example.authservice.model.UserTokens;
import org.example.authservice.repository.UserTokensRepository;
import org.example.authservice.service.TokenService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final UserTokensRepository userTokensRepository;

    @Override
    public void persist(User owner, Map<TokenType, String> jwtTokens) {
        Token accessToken = new Token(TokenType.ACCESS, jwtTokens.get(TokenType.ACCESS), false);
        Token refreshToken = new Token(TokenType.REFRESH, jwtTokens.get(TokenType.REFRESH), false);

        UserTokens userTokens = new UserTokens();
        userTokens.setOwnerEmail(owner.getEmail());
        userTokens.setAccessToken(accessToken);
        userTokens.setRefreshToken(refreshToken);

        userTokensRepository.save(userTokens);
    }
}
