package org.example.authservice.exception.exceptions;

/**
 * Author: Simeon Popov
 * Date of creation: 5/20/2024
 */
public class UserJwtTokensNotFoundException extends RuntimeException {
    public UserJwtTokensNotFoundException(String message, String userEmail) {
        super(String.format("%s %s %s", message, " | Subject: ", userEmail));
    }
}
