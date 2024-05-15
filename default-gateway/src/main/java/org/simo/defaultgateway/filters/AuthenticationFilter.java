package org.simo.defaultgateway.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.simo.defaultgateway.response.JwtValidationResponse;
import org.simo.defaultgateway.service.HeaderService;
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
public class AuthenticationFilter implements GatewayFilter {

    private final HeaderService headerService;
    private final JwtValidatorService jwtValidatorService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.trace("ENTERING: AuthenticationFilter");

        ServerHttpRequest request = exchange.getRequest();
        String authHeader = headerService.getAuthHeader(request);
        String jwtToken = headerService.getJwtFromHeader(authHeader);

        Mono<Void> voidMono = jwtValidatorService
                .isJwtValid(jwtToken)
                .flatMap(handleJwtValidationResult(exchange, chain));

        return voidMono;
//        return chain.filter(exchange);
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
