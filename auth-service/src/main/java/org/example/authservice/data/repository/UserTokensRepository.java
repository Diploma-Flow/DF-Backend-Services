package org.example.authservice.data.repository;

import org.example.authservice.data.entity.UserTokens;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */
public interface UserTokensRepository extends MongoRepository<UserTokens, String> {

    @Query("{ 'ownerEmail' : ?0 }")
    UserTokens findByOwnerEmail(String ownerEmail);
}
