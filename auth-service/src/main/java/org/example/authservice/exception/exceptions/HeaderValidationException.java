package org.example.authservice.exception.exceptions;

/**
 * Author: Simeon Popov
 * Date of creation: 6/11/2024
 */
public class HeaderValidationException extends RuntimeException {
    public HeaderValidationException() {
    }

    public HeaderValidationException(String message) {
        super(message);
    }

    public HeaderValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
