package org.simo.defaultgateway.gate;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.simo.defaultgateway.service.RouteValidatorService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Author: Simeon Popov
 * Date of creation: 6/6/2024
 */

@Log4j2
@RequiredArgsConstructor
public abstract class GateKeeper implements GlobalFilter, Ordered {
    private final RouteValidatorService routeValidatorService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.trace("ENTERING: Access checking");

        String path = exchange
                .getRequest()
                .getPath()
                .pathWithinApplication()
                .toString();

        boolean isPublic = routeValidatorService.isRoutePublic(path);
        log.info("Request path: {} is Public: {}", path, isPublic);

        if (isPublic) {
            log.info("Request forwarded to PUBLIC filter flow");
            return chain.filter(exchange);
        }

        log.info("Request forwarded to PRIVATE filter flow");
        return doFilter(exchange, chain);
    }

    protected abstract Mono<Void> doFilter(ServerWebExchange exchange, GatewayFilterChain chain);
}
