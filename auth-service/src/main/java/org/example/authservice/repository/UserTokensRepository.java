package org.example.authservice.repository;

import org.example.authservice.data.Token;
import org.example.authservice.model.UserTokens;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */
public interface UserTokensRepository extends MongoRepository<UserTokens, String> {

    @Query("{ 'ownerEmail' : ?0 }")
    Optional<UserTokens> findByOwnerEmail(String ownerEmail);

    UserTokens deleteUserTokensByOwnerEmail(String ownerEmail);

    boolean existsByOwnerEmail(String ownerEmail);
}