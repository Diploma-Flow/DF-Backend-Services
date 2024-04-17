package org.simo.defaultgateway.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.simo.defaultgateway.service.RouteValidatorService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
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
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    private final RouteValidatorService routeValidatorService;
    private final JwtValidationGatewayFilterFactory jwtValidationGatewayFilterFactory;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("ENTERING: CustomGlobalFilter");
        String path = exchange
                .getRequest()
                .getPath()
                .pathWithinApplication()
                .toString();

        boolean isPublic = routeValidatorService.isRoutePublic(path);
        log.info("Request path: {} is Public: {}", path, isPublic);

        if (isPublic) {
            log.info("Request forwarded to normal filter flow");
            return chain.filter(exchange);
        }

        log.info("Request forwarded to header and jwt filters");
        return jwtValidationGatewayFilterFactory.filter(exchange, chain);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
