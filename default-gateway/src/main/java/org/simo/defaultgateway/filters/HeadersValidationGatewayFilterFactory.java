package org.simo.defaultgateway.filters;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Author: Simeon Popov
 * Date of creation: 7.4.2024 г.
 */

@Component
@Log4j2
public class HeadersValidationGatewayFilterFactory implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("ENTERING: HeadersValidationGatewayFilterFactory");

        ServerHttpRequest request = exchange.getRequest();

        if (isAuthMissing(request)) {
            String errorMessage = "Authorization header is missing in request";
            log.warn(errorMessage);

            return onError(exchange, HttpStatus.UNAUTHORIZED, errorMessage);
        }

        if (isAuthHeaderBlank(request)) {
            String errorMessage = "Authorization header is empty";
            log.warn(errorMessage);

            return onError(exchange, HttpStatus.UNAUTHORIZED, errorMessage);
        }

        log.info("Authorization header validated successfully");
        return chain.filter(exchange);
    }

    public static Mono<Void> onError(ServerWebExchange exchange, HttpStatus status, String message) {
        exchange
                .getResponse()
                .setStatusCode(status);

        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange
                .getResponse()
                .bufferFactory()
                .wrap(bytes);

        return exchange
                .getResponse()
                .writeWith(Mono.just(buffer))
                .then(Mono.error(new IllegalStateException(message)));
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request
                .getHeaders()
                .containsKey("Authorization");
    }

    private boolean isAuthHeaderBlank(ServerHttpRequest request) {
        return getAuthHeader(request).isBlank();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request
                .getHeaders()
                .getOrEmpty("Authorization")
                .get(0);
    }
}
