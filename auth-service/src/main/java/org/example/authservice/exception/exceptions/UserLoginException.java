package org.example.authservice.exception.exceptions;

/**
 * Author: Simeon Popov
 * Date of creation: 5/16/2024
 */
public class UserLoginException extends RuntimeException{
    public UserLoginException(String message) {
        super(message);
    }
}
