package org.example.authservice.exception.exceptions;

/**
 * Author: Simeon Popov
 * Date of creation: 5/13/2024
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
