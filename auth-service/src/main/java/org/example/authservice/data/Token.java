package org.example.authservice.data;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Author: Simeon Popov
 * Date of creation: 11.1.2024 г.
 */

@Data
@AllArgsConstructor
public class Token {
    private final TokenType type;
    private final String value;
    private final boolean isRevoked;
}
