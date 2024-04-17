package org.simo.defaultgateway.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

        if (jwtValidatorService.isAuthMissing(request)) {
            String errorMessage = "Authorization header is missing in request";
            log.warn(errorMessage);

            return onError(exchange, HttpStatus.UNAUTHORIZED, errorMessage);
        }

        if (jwtValidatorService.isAuthHeaderBlank(request)) {
            String errorMessage = "Authorization header is empty";
            log.warn(errorMessage);

            return onError(exchange, HttpStatus.UNAUTHORIZED, errorMessage);
        }

        if (jwtValidatorService.isBearerMissing(request)) {
            String errorMessage = "Bearer token is missing";
            log.warn(errorMessage);

            return onError(exchange, HttpStatus.UNAUTHORIZED, errorMessage);
        }

        if (!jwtValidatorService.isJwtFormatValid(request)) {
            String errorMessage = "JWT format is NOT valid";
            log.warn(errorMessage);

            return onError(exchange, HttpStatus.UNAUTHORIZED, errorMessage);
        }

        return jwtValidatorService
                .isJwtValid(request)
                .flatMap(handleJwtValidationResult(exchange, chain));
    }

    private static Function<Boolean, Mono<? extends Void>> handleJwtValidationResult(ServerWebExchange exchange, GatewayFilterChain chain) {
        return valid -> {
            if (valid) {
                log.info("JWT is valid");
                return chain.filter(exchange); // Proceed with the request
            }

            String errorMessage = "JWT is NOT valid";
            log.warn(errorMessage);

            return onError(exchange, HttpStatus.UNAUTHORIZED, errorMessage);
        };
    }
}
