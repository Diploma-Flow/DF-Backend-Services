package org.example.authservice.exception.exceptions;

/**
 * Author: Simeon Popov
 * Date of creation: 5/21/2024
 */
public class UserAlreadyLoggedOutException extends RuntimeException {
    public UserAlreadyLoggedOutException() {
    }

    public UserAlreadyLoggedOutException(String message) {
        super(message);
    }
}
