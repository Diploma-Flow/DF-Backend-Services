package org.simo.defaultgateway.validators.impl;

import org.apache.commons.lang.StringUtils;
import org.simo.defaultgateway.exception.HeaderValidationException;
import org.simo.defaultgateway.validators.AuthValidator;

/**
 * Author: Simeon Popov
 * Date of creation: 5/14/2024
 */
public class JwtFormatValidator implements AuthValidator {
    private static final String JWT_VALIDATION_REGEX = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_.+/=]*$";

    @Override
    public void validate(String authHeader) throws HeaderValidationException {
        String token = authHeader.substring(7);

        if (StringUtils.isBlank(token) || !token.matches(JWT_VALIDATION_REGEX)) {
            throw new HeaderValidationException("JWT format is NOT valid");
        }
    }
}
