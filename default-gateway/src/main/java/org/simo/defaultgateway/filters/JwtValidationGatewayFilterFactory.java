package org.simo.defaultgateway.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.simo.defaultgateway.exception.AuthenticationException;
import org.simo.defaultgateway.response.JwtValidationResponse;
import org.simo.defaultgateway.service.JwtValidatorService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.simo.defaultgateway.utils.WebFluxUtils.onError;

/**
 * Author: Simeon Popov
 * Date of creation: 7.4.2024 г.
 */

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtValidationGatewayFilterFactory implements GatewayFilter {

    private final JwtValidatorService jwtValidatorService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.trace("ENTERING: JwtValidationGatewayFilterFactory");

        ServerHttpRequest request = exchange.getRequest();

        try {
            jwtValidatorService.validateAuth(request);
            jwtValidatorService.validateAuthHeader(request);
            jwtValidatorService.validateBearer(request);
            jwtValidatorService.validateJwtFormat(request);

        } catch (AuthenticationException e) {
            log.warn(e.getMessage());
            return onError(exchange, HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        return jwtValidatorService
                .isJwtValid(request)
                .flatMap(handleJwtValidationResult(exchange, chain));
    }

    private static Function<JwtValidationResponse, Mono<? extends Void>> handleJwtValidationResult(ServerWebExchange exchange, GatewayFilterChain chain) {
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
