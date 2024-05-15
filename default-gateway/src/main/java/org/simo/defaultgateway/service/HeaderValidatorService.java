package org.simo.defaultgateway.service;

import org.apache.commons.lang.StringUtils;
import org.simo.defaultgateway.exception.HeaderValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Author: Simeon Popov
 * Date of creation: 5/16/2024
 */

@Service
public class HeaderValidatorService {
    public static final String BEARER_PREFIX = "Bearer ";
    private static final String JWT_VALIDATION_REGEX = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_.+/=]*$";



    public static String getAuthHeader(ServerHttpRequest request) {
        return Optional
                .ofNullable(request
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION))
                .orElseThrow(() -> new HeaderValidationException("Authorization header is missing in request"));
    }

    public void validateAuthHeader(String authHeader) {
        if (StringUtils.isBlank(authHeader)) {
            throw new HeaderValidationException("Authorization header is empty");
        }

        if (!authHeader.startsWith(BEARER_PREFIX)) {
            throw new HeaderValidationException("Bearer token is missing");
        }

        String token = authHeader.substring(7);

        if (!token.matches(JWT_VALIDATION_REGEX)) {
            throw new HeaderValidationException("JWT format is NOT valid");
        }
    }
}
