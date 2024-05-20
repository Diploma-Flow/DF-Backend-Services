package org.example.authservice.exception.exceptions;


/**
 * Author: Simeon Popov
 * Date of creation: 5/19/2024
 */
public class UserServiceConnectionTimeoutException extends RuntimeException{

    public UserServiceConnectionTimeoutException(String message) {
        super(message);
    }
}
