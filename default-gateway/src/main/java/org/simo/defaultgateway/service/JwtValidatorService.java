package org.simo.defaultgateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.simo.defaultgateway.exception.JwtValidationException;
import org.simo.defaultgateway.response.JwtValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.simo.defaultgateway.utils.WebFluxUtils.onError;


/**
 * Author: Simeon Popov
 * Date of creation: 17.4.2024 г.
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class JwtValidatorService {

    private final WebClient.Builder webClientBuilder;

    @Value("${auth-service.validation.url}")
    private String AUTH_SERVICE_VALIDATION_URL;

    public Mono<JwtValidationResponse> isJwtValid(String jwtToken) {
        WebClient.RequestHeadersSpec<?> request = generateRequestWithBody(jwtToken);
        Mono<JwtValidationResponse> jwtValidationResponseMono = sendValidationRequest(request);

        return jwtValidationResponseMono;
    }

    private Mono<JwtValidationResponse> sendValidationRequest(WebClient.RequestHeadersSpec<?> request) {
        return request.exchangeToMono(mapResponseToJwtValidationResponse())
                .onErrorMap(throwable -> new JwtValidationException("Error during JWT validation: " + throwable.getMessage()));
    }

    private Function<ClientResponse, Mono<JwtValidationResponse>> mapResponseToJwtValidationResponse() {
        return response -> {

            HttpStatusCode httpStatusCode = response.statusCode();

            if (httpStatusCode.is2xxSuccessful() || httpStatusCode.is4xxClientError()) {
                return response.bodyToMono(JwtValidationResponse.class);
            }

            throw new JwtValidationException("Unexpected response status from user-service: " + httpStatusCode);
        };
    }

    private WebClient.RequestHeadersSpec<?> generateRequestWithBody(String token) {
        return webClientBuilder
                .build()
                .post()
                .uri(AUTH_SERVICE_VALIDATION_URL)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(token);
    }

    public Function<JwtValidationResponse, Mono<? extends Void>> processValidationResult(ServerWebExchange exchange, GatewayFilterChain chain) {
        return response -> {
            HttpStatus responseHttpStatus = response.getHttpStatus();

            if (responseHttpStatus.is2xxSuccessful()) {
                log.info("JWT is valid");
                return chain.filter(exchange);
            }

            if (responseHttpStatus.is4xxClientError()) {
                String errorMessage = response.getResponse();
                log.warn(errorMessage);

                return onError(exchange, HttpStatus.UNAUTHORIZED, errorMessage);
            }

            if (responseHttpStatus.is5xxServerError()) {
                String errorMessage = "Internal server error";
                log.error(errorMessage);

                return onError(exchange, HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
            }

            return onError(exchange, HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error");
        };
    }
}
