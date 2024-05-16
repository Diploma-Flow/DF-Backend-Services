package org.simo.defaultgateway.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.simo.defaultgateway.service.HeaderService;
import org.simo.defaultgateway.service.JwtValidatorService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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

        return jwtValidatorService
                .isJwtValid(jwtToken)
                .flatMap(jwtValidatorService.processValidationResult(exchange, chain));
    }
}
