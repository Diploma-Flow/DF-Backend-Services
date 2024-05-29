package org.example.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.authservice.model.UserToken;
import org.example.authservice.repository.UserTokenRepository;
import org.example.authservice.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private static final Logger log = LoggerFactory.getLogger(TokenServiceImpl.class);
    private final UserTokenRepository userTokenRepository;

    @Override
    public void saveToken(UserToken userToken) {
        userTokenRepository.save(userToken);
    }

//    @Override
//    public void persist(User owner, Map<TokenType, String> jwtTokens) {
//        Token accessToken = new Token(TokenType.ACCESS, jwtTokens.get(TokenType.ACCESS), false);
//        Token refreshToken = new Token(TokenType.REFRESH, jwtTokens.get(TokenType.REFRESH), false);
//
//        UserToken userToken = new UserToken();
//        userToken.setOwnerEmail(owner.getEmail());
//        userToken.setAccessTokens(List.of(accessToken));
//        userToken.setRefreshToken(refreshToken);
//
//        userTokenRepository.save(userToken);
//    }
//
//    @Override
//    public void updateTokens(String userEmail, Map<TokenType, String> jwtTokens) {
//        UserToken userToken = getTokens(userEmail);
//
//        Token accessToken = new Token(TokenType.ACCESS, jwtTokens.get(TokenType.ACCESS), false);
//        Token refreshToken = new Token(TokenType.REFRESH, jwtTokens.get(TokenType.REFRESH), false);
//
//        if (userToken != null) {
//            List<Token> accessTokens = userToken.getAccessTokens();
//
//            if(accessTokens != null && !accessTokens.contains(accessToken)) {
//                accessTokens.add(accessToken);
//            }
//
//            userToken.setAccessTokens(accessTokens);
//            userToken.setRefreshToken(refreshToken);
//            userTokenRepository.save(userToken);
//        }
//    }
//
//    @Override
//    public UserToken getTokens(String userEmail) {
//        Optional<UserToken> userTokens = userTokenRepository.findByOwnerEmail(userEmail);
//        return userTokens.orElseThrow(()-> new UserJwtTokensNotFoundException("User tokens not found", userEmail));
//    }
//
//    @Override
//    public Token findAccessTokenByValueAndEmail(String tokenValue, String subjectEmail) {
//        UserToken userToken = getTokens(subjectEmail);
//
//        List<Token> accessTokens = Optional.ofNullable(userToken)
//                .map(UserToken::getAccessTokens)
//                .orElseThrow(() -> new InvalidJwtTokenException("NO SUCH ACCESS TOKEN FOUND FOR USER: " + subjectEmail));
//
//        Token accessToken = accessTokens
//                .stream()
//                .filter(Objects::nonNull)
//                .filter(token -> token.getValue() != null && token.getValue().equals(tokenValue))
//                .filter(token -> token.getType() != null && token.getType().equals(TokenType.ACCESS))
//                .findFirst()
//                .orElseThrow(() -> new InvalidJwtTokenException("NO SUCH ACCESS TOKEN FOUND FOR USER: " + subjectEmail));
//
//        return accessToken;
//    }
//
//    @Override
//    public Token findRefreshTokenByValueAndEmail(String jwtToken, String subjectEmail) {
//        UserToken userToken = getTokens(subjectEmail);
//
//        Token refreshToken = userToken.getRefreshToken();
//
//        if (refreshToken == null || refreshToken.getType() != TokenType.REFRESH || !jwtToken.equals(refreshToken.getValue())) {
//            throw new InvalidJwtTokenException("NO SUCH REFRESH TOKEN FOUND FOR USER: " + subjectEmail);
//        }
//
//        return refreshToken;
//    }
//
//    @Override
//    public void deleteTokensOf(String subjectEmail) {
//        if (!userTokenRepository.existsByOwnerEmail(subjectEmail)) {
//            throw new UserAlreadyLoggedOutException("User already logged out");
//        }
//
//        userTokenRepository.deleteUserTokensByOwnerEmail(subjectEmail);
//        log.info("Logged out user tokens for {}", subjectEmail);
//    }
}
