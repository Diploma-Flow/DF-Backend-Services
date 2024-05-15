package org.simo.defaultgateway.validators;

import org.simo.defaultgateway.exception.HeaderValidationException;

/**
 * Author: Simeon Popov
 * Date of creation: 5/14/2024
 */

@FunctionalInterface
public interface AuthValidator {

    void validate(String authHeader) throws HeaderValidationException;

    default AuthValidator andThen(AuthValidator next) {
        return request -> {
            validate(request);
            next.validate(request);
        };
    }
}
