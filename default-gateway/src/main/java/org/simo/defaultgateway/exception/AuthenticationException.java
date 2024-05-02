package org.simo.defaultgateway.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Author: Simeon Popov
 * Date of creation: 2.5.2024 г.
 */

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
