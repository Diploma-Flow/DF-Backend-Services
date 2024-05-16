package org.simo.defaultgateway.exception.exceptions;

/**
 * Author: Simeon Popov
 * Date of creation: 2.5.2024 г.
 */

public class HeaderValidationException extends RuntimeException {
    public HeaderValidationException(String message) {
        super(message);
    }
}
