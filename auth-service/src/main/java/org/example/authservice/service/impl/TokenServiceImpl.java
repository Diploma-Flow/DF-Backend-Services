package org.example.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.authservice.data.Token;
import org.example.authservice.data.TokenType;
import org.example.authservice.data.entity.User;
import org.example.authservice.exception.exceptions.InvalidJwtTokenException;
import org.example.authservice.exception.exceptions.UserAlreadyLoggedOutException;
import org.example.authservice.exception.exceptions.UserJwtTokensNotFoundException;
import org.example.authservice.exception.exceptions.UserNotFoundException;
import org.example.authservice.model.UserTokens;
import org.example.authservice.repository.UserTokensRepository;
import org.example.authservice.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private static final Logger log = LoggerFactory.getLogger(TokenServiceImpl.class);
    private final UserTokensRepository userTokensRepository;

    @Override
    public void persist(User owner, Map<TokenType, String> jwtTokens) {
        Token accessToken = new Token(TokenType.ACCESS, jwtTokens.get(TokenType.ACCESS), false);
        Token refreshToken = new Token(TokenType.REFRESH, jwtTokens.get(TokenType.REFRESH), false);

        UserTokens userTokens = new UserTokens();
        userTokens.setOwnerEmail(owner.getEmail());
        userTokens.setAccessTokens(List.of(accessToken));
        userTokens.setRefreshToken(refreshToken);

        userTokensRepository.save(userTokens);
    }

    @Override
    public void updateTokens(String userEmail, Map<TokenType, String> jwtTokens) {
        UserTokens userTokens = getTokens(userEmail);

        Token accessToken = new Token(TokenType.ACCESS, jwtTokens.get(TokenType.ACCESS), false);
        Token refreshToken = new Token(TokenType.REFRESH, jwtTokens.get(TokenType.REFRESH), false);

        if (userTokens != null) {
            List<Token> accessTokens = userTokens.getAccessTokens();

            if(accessTokens != null && !accessTokens.contains(accessToken)) {
                accessTokens.add(accessToken);
            }

            userTokens.setAccessTokens(accessTokens);
            userTokens.setRefreshToken(refreshToken);
            userTokensRepository.save(userTokens);
        }
    }

    @Override
    public UserTokens getTokens(String userEmail) {
        Optional<UserTokens> userTokens = userTokensRepository.findByOwnerEmail(userEmail);
        return userTokens.orElseThrow(()-> new UserJwtTokensNotFoundException("User tokens not found", userEmail));
    }

    @Override
    public Token findAccessTokenByValueAndEmail(String tokenValue, String subjectEmail) {
        UserTokens userTokens = getTokens(subjectEmail);

        List<Token> accessTokens = Optional.ofNullable(userTokens)
                .map(UserTokens::getAccessTokens)
                .orElseThrow(() -> new InvalidJwtTokenException("NO SUCH ACCESS TOKEN FOUND FOR USER: " + subjectEmail));

        Token accessToken = accessTokens
                .stream()
                .filter(Objects::nonNull)
                .filter(token -> token.getValue() != null && token.getValue().equals(tokenValue))
                .filter(token -> token.getType() != null && token.getType().equals(TokenType.ACCESS))
                .findFirst()
                .orElseThrow(() -> new InvalidJwtTokenException("NO SUCH ACCESS TOKEN FOUND FOR USER: " + subjectEmail));

        return accessToken;
    }

    @Override
    public Token findRefreshTokenByValueAndEmail(String jwtToken, String subjectEmail) {
        UserTokens userTokens = getTokens(subjectEmail);

        Token refreshToken = userTokens.getRefreshToken();

        if (refreshToken == null || refreshToken.getType() != TokenType.REFRESH || !jwtToken.equals(refreshToken.getValue())) {
            throw new InvalidJwtTokenException("NO SUCH REFRESH TOKEN FOUND FOR USER: " + subjectEmail);
        }

        return refreshToken;
    }

    @Override
    public void deleteTokensOf(String subjectEmail) {
        if (!userTokensRepository.existsByOwnerEmail(subjectEmail)) {
            throw new UserAlreadyLoggedOutException("User already logged out");
        }

        userTokensRepository.deleteUserTokensByOwnerEmail(subjectEmail);
        log.info("Logged out user tokens for {}", subjectEmail);
    }
}
