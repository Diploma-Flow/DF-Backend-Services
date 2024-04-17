package org.simo.defaultgateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.simo.defaultgateway.response.JwtValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

    private static final String JWT_VALIDATION_REGEX = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_.+/=]*$";
    private final WebClient.Builder webClientBuilder;
    @Value("${auth-service.validation.url}")
    private String AUTH_SERVICE_VALIDATION_URL;

    public boolean isAuthMissing(ServerHttpRequest request) {
        return !request
                .getHeaders()
                .containsKey(HttpHeaders.AUTHORIZATION);
    }

    public boolean isAuthHeaderBlank(ServerHttpRequest request) {
        return getAuthHeader(request).isBlank();
    }

    public String getAuthHeader(ServerHttpRequest request) {
        return request
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);
    }

    public boolean isBearerMissing(ServerHttpRequest request) {
        return !getAuthHeader(request).startsWith("Bearer ");
    }

    public boolean isJwtFormatValid(ServerHttpRequest request) {
        String token = getAuthHeader(request).substring(7);
        return !token.isBlank() && token.matches(JWT_VALIDATION_REGEX);
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
                    if (response.statusCode().is4xxClientError() || response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(JwtValidationResponse.class);
                    }

                    return response.createError();
                });
    }
}
