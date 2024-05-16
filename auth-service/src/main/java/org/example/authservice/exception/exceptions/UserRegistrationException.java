package org.example.authservice.exception.exceptions;

/**
 * Author: Simeon Popov
 * Date of creation: 5/16/2024
 */
public class UserRegistrationException extends RuntimeException{
    public UserRegistrationException(String message) {
        super(message);
    }
}
