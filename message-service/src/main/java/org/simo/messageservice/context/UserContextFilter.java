package org.simo.messageservice.context;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Author: Simeon Popov
 * Date of creation: 6/11/2024
 */
@Component
public class UserContextFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String userEmail = exchange.getRequest().getHeaders().getFirst("X-User-Email");
        String userRole = exchange.getRequest().getHeaders().getFirst("X-User-Role");

        // Populate RequestContext with extracted details
        RequestContext requestContext = RequestContextHolder.getContext();
        requestContext.setUserEmail(userEmail);
        requestContext.setUserRole(userRole);

        return chain.filter(exchange).doFinally(signalType -> {
            // Clear the context after the request is completed
            RequestContextHolder.clear();
        });
    }
}
