package org.example.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.authservice.data.Token;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user-tokens")
public class UserTokens {

    @Id
    private String id;

    @Indexed(unique = true)
    private String ownerEmail;

    private List<Token> accessTokens;
    private Token refreshToken;
}
