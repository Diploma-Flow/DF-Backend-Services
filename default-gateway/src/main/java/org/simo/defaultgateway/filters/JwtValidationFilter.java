package org.simo.defaultgateway.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.simo.defaultgateway.gate.GateKeeper;
import org.simo.defaultgateway.response.JwtValidationResponse;
import org.simo.defaultgateway.service.HeaderService;
import org.simo.defaultgateway.service.JwtValidatorService;
import org.simo.defaultgateway.service.RouteValidatorService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Author: Simeon Popov
 * Date of creation: 6/6/2024
 */

@Log4j2
@Component
public class JwtValidationFilter extends GateKeeper {
    private final HeaderService headerService;
    private final JwtValidatorService jwtValidatorService;

    public JwtValidationFilter(RouteValidatorService routeValidatorService, HeaderService headerService, JwtValidatorService jwtValidatorService) {
        super(routeValidatorService);
        this.headerService = headerService;
        this.jwtValidatorService = jwtValidatorService;
    }

    @Override
    protected Mono<Void> doFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.trace("ENTERING: JwtValidationFilter");
        ServerHttpRequest request = exchange.getRequest();

        String authHeader = headerService.getAuthHeader(request);
        String jwtToken = headerService.getJwtFromHeader(authHeader);

        Mono<JwtValidationResponse> jwtValidationResponseMono = jwtValidatorService.isJwtValid(jwtToken);
        return jwtValidationResponseMono.flatMap(jwtValidatorService.processValidationResult(exchange, chain));
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
