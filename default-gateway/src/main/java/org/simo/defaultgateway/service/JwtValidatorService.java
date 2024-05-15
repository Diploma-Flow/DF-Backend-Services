package org.simo.defaultgateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.simo.defaultgateway.exception.HeaderValidationException;
import org.simo.defaultgateway.response.JwtValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.simo.defaultgateway.service.HeaderValidatorService.getAuthHeader;

/**
 * Author: Simeon Popov
 * Date of creation: 17.4.2024 г.
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class JwtValidatorService {

    public static final int JWT_BEGINNING_INDEX = 7;
    private final WebClient.Builder webClientBuilder;

    @Value("${auth-service.validation.url}")
    private String AUTH_SERVICE_VALIDATION_URL;

    public Mono<JwtValidationResponse> isJwtValid(ServerHttpRequest request) {
        String token = getAuthHeader(request).substring(JWT_BEGINNING_INDEX);

        return generateRequestWithBody(token)
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

    private WebClient.RequestHeadersSpec<?> generateRequestWithBody(String token) {
        return webClientBuilder
                .build()
                .post()
                .uri(AUTH_SERVICE_VALIDATION_URL)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(token);
    }
}
