package org.simo.defaultgateway.validators;

import org.simo.defaultgateway.exception.AuthenticationException;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * Author: Simeon Popov
 * Date of creation: 5/14/2024
 */

@FunctionalInterface
public interface AuthValidator {

    void validate(String authHeader) throws AuthenticationException;

    default AuthValidator andThen(AuthValidator next) {
        return request -> {
            validate(request);
            next.validate(request);
        };
    }
}
