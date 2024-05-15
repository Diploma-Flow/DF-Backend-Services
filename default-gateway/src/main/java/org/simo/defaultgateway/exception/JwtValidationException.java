package org.simo.defaultgateway.exception;

/**
 * Author: Simeon Popov
 * Date of creation: 5/16/2024
 */
public class JwtValidationException extends RuntimeException{
    public JwtValidationException(String message) {
        super(message);
    }
}
