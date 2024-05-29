package org.example.authservice.repository;

import org.example.authservice.model.UserToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */
public interface UserTokenRepository extends MongoRepository<UserToken, String> {

//    @Query("{ 'ownerEmail' : ?0 }")
//    Optional<UserToken> findByOwnerEmail(String ownerEmail);
//
//    UserToken deleteUserTokensByOwnerEmail(String ownerEmail);
//
//    boolean existsByOwnerEmail(String ownerEmail);
}