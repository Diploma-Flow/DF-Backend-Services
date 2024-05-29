package org.example.authservice.repository;

import org.example.authservice.model.UserToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */
public interface UserTokenRepository extends MongoRepository<UserToken, String> {

    Optional<UserToken> findByValue(String jwtToken);

    List<UserToken> findByOwnerEmail(String ownerEmail);

    List<UserToken> findByOwnerEmailAndIsRevokedFalse(String ownerEmail);
}