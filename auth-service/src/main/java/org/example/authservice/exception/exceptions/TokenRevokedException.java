package org.example.authservice.exception.exceptions;

/**
 * Author: Simeon Popov
 * Date of creation: 5/30/2024
 */
public class TokenRevokedException extends RuntimeException {

    public TokenRevokedException() {
    }

    public TokenRevokedException(String message) {
        super(message);
    }
}
