package org.example.authservice.exception.exceptions;

/**
 * Author: Simeon Popov
 * Date of creation: 5/13/2024
 */
public class UserServiceInternalServerError extends RuntimeException {
    public UserServiceInternalServerError() {
    }

    public UserServiceInternalServerError(String message) {
        super(message);
    }
}
