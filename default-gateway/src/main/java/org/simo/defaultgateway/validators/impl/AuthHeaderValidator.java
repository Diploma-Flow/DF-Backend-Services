package org.simo.defaultgateway.validators.impl;

import org.apache.commons.lang.StringUtils;
import org.simo.defaultgateway.exception.AuthenticationException;
import org.simo.defaultgateway.validators.AuthValidator;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * Author: Simeon Popov
 * Date of creation: 5/14/2024
 */
public class AuthHeaderValidator implements AuthValidator {
    @Override
    public void validate(String authHeader) throws AuthenticationException {
        if (StringUtils.isBlank(authHeader)) {
            throw new AuthenticationException("Authorization header is empty");
        }
    }
}
