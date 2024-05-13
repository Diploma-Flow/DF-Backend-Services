package org.example.authservice.exception.exceptions;

/**
 * Author: Simeon Popov
 * Date of creation: 8.5.2024 г.
 */
public class InvalidLoginCredentialsException extends RuntimeException{
    public InvalidLoginCredentialsException(String message) {
        super(message);
    }

    public InvalidLoginCredentialsException() {
        super("Invalid email or password");
    }
}
