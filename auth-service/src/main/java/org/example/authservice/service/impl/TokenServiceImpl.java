package org.example.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.authservice.exception.exceptions.InvalidJwtTokenException;
import org.example.authservice.exception.exceptions.TokenNotFoundException;
import org.example.authservice.exception.exceptions.TokenRevokedException;
import org.example.authservice.model.UserToken;
import org.example.authservice.repository.UserTokenRepository;
import org.example.authservice.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public UserToken getTokenByValue(String jwtToken) {
        Optional<UserToken> optionalUserToken = userTokenRepository.findByValue(jwtToken);
        return optionalUserToken.orElseThrow(()-> new TokenNotFoundException("Token not found!"));
    }

    @Override
    public void verifyNotRevoked(UserToken userToken) {
        if (userToken.isRevoked()) {
            throw new TokenRevokedException("Token is revoked!");
        }
    }

    @Override
    public void revokeToken(UserToken userToken) {
        userToken.setRevoked(true);
        saveToken(userToken);
    }

    @Override
    public void revokeAllOwnedBy(String subjectEmail) {
        List<UserToken> allSubjectTokens = userTokenRepository.findByOwnerEmailAndIsRevokedFalse(subjectEmail);
        for (UserToken ut: allSubjectTokens){
            ut.setRevoked(true);
        }

        userTokenRepository.saveAll(allSubjectTokens);
    }
}
