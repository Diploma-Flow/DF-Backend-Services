package org.simo.defaultgateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.simo.defaultgateway.exception.AuthenticationException;
import org.simo.defaultgateway.response.JwtValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Author: Simeon Popov
 * Date of creation: 17.4.2024 г.
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class JwtValidatorService {

    public static final String BEARER_PREFIX = "Bearer ";
    private static final String JWT_VALIDATION_REGEX = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_.+/=]*$";
    private final WebClient.Builder webClientBuilder;
    @Value("${auth-service.validation.url}")
    private String AUTH_SERVICE_VALIDATION_URL;

    public void validateAuth(ServerHttpRequest request) {
        boolean containsAuthHeader = request
                .getHeaders()
                .containsKey(HttpHeaders.AUTHORIZATION);

        if (!containsAuthHeader) {
            throw new AuthenticationException("Authorization header is missing in request");
        }
    }

    public void validateAuthHeader(ServerHttpRequest request) {
        String authHeader = getAuthHeader(request);

        if (authHeader.isBlank()) {
            throw new AuthenticationException("Authorization header is empty");
        }
    }

    public String getAuthHeader(ServerHttpRequest request) {
        return request
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);
    }

    public void validateBearer(ServerHttpRequest request) {
        String authHeader = getAuthHeader(request);

        if (!authHeader.startsWith(BEARER_PREFIX)) {
            throw new AuthenticationException("Bearer token is missing");
        }
    }

    public void validateJwtFormat(ServerHttpRequest request) {
        String token = getAuthHeader(request).substring(7);

        if (token.isBlank() || !token.matches(JWT_VALIDATION_REGEX)) {
            throw new AuthenticationException("JWT format is NOT valid");
        }
    }

    public Mono<JwtValidationResponse> isJwtValid(ServerHttpRequest request) {
        String token = getAuthHeader(request).substring(7);

        return webClientBuilder
                .build()
                .post()
                .uri(AUTH_SERVICE_VALIDATION_URL)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(token)
                .exchangeToMono(response -> {
                    if (response
                            .statusCode()
                            .is4xxClientError() || response
                            .statusCode()
                            .is2xxSuccessful()) {
                        return response.bodyToMono(JwtValidationResponse.class);
                    }

                    return response.createError();
                });
    }
}
