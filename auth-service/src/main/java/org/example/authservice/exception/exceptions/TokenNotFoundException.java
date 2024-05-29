package org.example.authservice.exception.exceptions;

/**
 * Author: Simeon Popov
 * Date of creation: 5/30/2024
 */
public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException() {
    }

    public TokenNotFoundException(String message) {
        super(message);
    }
}
