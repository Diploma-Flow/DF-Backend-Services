package org.example.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.authservice.enums.TokenType;

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
