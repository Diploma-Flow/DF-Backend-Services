package org.simo.defaultgateway.validators.impl;

import org.apache.commons.lang.StringUtils;
import org.simo.defaultgateway.exception.HeaderValidationException;
import org.simo.defaultgateway.validators.AuthValidator;

/**
 * Author: Simeon Popov
 * Date of creation: 5/14/2024
 */
public class AuthHeaderValidator implements AuthValidator {
    @Override
    public void validate(String authHeader) throws HeaderValidationException {
        if (StringUtils.isBlank(authHeader)) {
            throw new HeaderValidationException("Authorization header is empty");
        }
    }
}
