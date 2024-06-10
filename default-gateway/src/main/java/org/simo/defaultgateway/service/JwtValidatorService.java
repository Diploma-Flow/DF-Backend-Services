package org.simo.defaultgateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.simo.defaultgateway.exception.exceptions.JwtValidationException;
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

    private WebClient.RequestHeadersSpec<?> generateRequestWithBody(String token) {
        return webClientBuilder
                .build()
                .post()
                .uri(AUTH_SERVICE_VALIDATION_URL)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(token);
    }

    private Mono<JwtValidationResponse> sendValidationRequest(WebClient.RequestHeadersSpec<?> request) {
        return request
                .exchangeToMono(mapResponseToJwtValidationResponse())
                .onErrorMap(throwable -> new RuntimeException("Error during JWT validation: " + throwable.getMessage()));
    }

    private Function<ClientResponse, Mono<JwtValidationResponse>> mapResponseToJwtValidationResponse() {
        return response -> {

            HttpStatusCode httpStatusCode = response.statusCode();

            //responses with status '4xx' are mapped for later processing the reason of the client error
            if (httpStatusCode.is2xxSuccessful() || httpStatusCode.is4xxClientError()) {
                Mono<JwtValidationResponse> jwtValidationResponseMono = response.bodyToMono(JwtValidationResponse.class);

                return jwtValidationResponseMono.doOnNext(jwtResponse -> log.info("Response from user-service {}",jwtResponse));
            }

            throw new RuntimeException("Unexpected response status from user-service: " + httpStatusCode);
        };
    }

    public Function<JwtValidationResponse, Mono<? extends Void>> processValidationResult(ServerWebExchange exchange, GatewayFilterChain chain) {
        return response -> {
            HttpStatus responseHttpStatus = response.getHttpStatus();

            if (responseHttpStatus.is2xxSuccessful()) {
                log.info("JWT is valid");
                return chain.filter(exchange);
            }

            String errorMessage = "Unexpected response status from user-service: " + responseHttpStatus;

            if (responseHttpStatus.is4xxClientError()) {
                errorMessage = response.getResponse();
                log.warn(errorMessage);
                throw new JwtValidationException(errorMessage);
            }

            throw new RuntimeException(errorMessage);
        };
    }
}
