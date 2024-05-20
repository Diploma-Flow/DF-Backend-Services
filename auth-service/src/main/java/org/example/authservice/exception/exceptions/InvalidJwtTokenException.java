package org.example.authservice.exception.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Author: Simeon Popov
 * Date of creation: 5/20/2024
 */

public class InvalidJwtTokenException extends RuntimeException {

    public InvalidJwtTokenException(String message) {
        super(message);
    }

    public InvalidJwtTokenException() {
    }
}
