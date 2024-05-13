package org.example.userservice.exception.exceptions;/**
 * Author: Simeon Popov
 * Date of creation: 8.5.2024 г.
 */public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
