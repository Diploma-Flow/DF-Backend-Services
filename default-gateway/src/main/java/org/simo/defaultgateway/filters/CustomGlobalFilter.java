package org.simo.defaultgateway.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    private final RouteValidator routeValidator;
    private final HeadersValidationGatewayFilterFactory headersValidationGatewayFilterFactory;
    private final JwtValidationGatewayFilterFactory jwtValidationGatewayFilterFactory;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("ENTERING: CustomGlobalFilter");

        // Determine if the route is protected based on some criteria
        String path = exchange
                .getRequest()
                .getPath()
                .pathWithinApplication()
                .toString();

        boolean isProtected = routeValidator.isRouteProtected(path);
        log.info("Request path: {} is protected: {}", path, isProtected);

        boolean isPublic = routeValidator.isRoutePublic(path);
        log.info("Request path: {} is Public: {}", path, isPublic);

        if (isProtected) {
            log.info("Request forwarded to header and jwt filters");

            // If the route is protected, apply the headersValidationFilter and jwtValidationFilter
            return headersValidationGatewayFilterFactory
                    .filter(exchange, chain)
                    .then(jwtValidationGatewayFilterFactory.filter(exchange, chain));
        }

        log.info("Request forwarded to normal filter flow");
        // If the route is not protected, continue with the original filter chain
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
