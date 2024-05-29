package org.example.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.authservice.enums.TokenType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "user-tokens")
public class UserToken {

    @Id
    private String id;

    @Indexed
    private String ownerEmail;

    private TokenType type;
    private String value;

    @Builder.Default
    private boolean isRevoked = false;
}
