package org.example.authservice.service;

import org.example.authservice.exception.exceptions.TokenNotFoundException;
import org.example.authservice.exception.exceptions.TokenRevokedException;
import org.example.authservice.model.UserToken;

import java.util.Map;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */
public interface TokenService {
    void saveToken(UserToken userToken);

    UserToken getTokenByValue(String jwtToken) throws TokenNotFoundException;

    void verifyNotRevoked(UserToken userToken) throws TokenRevokedException;

    void revokeToken(UserToken userToken);

    void revokeAllOwnedBy(String subjectEmail);
}
