package org.simo.defaultgateway.filters;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
public class JwtValidationGatewayFilterFactory implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("ENTERING: JwtValidationGatewayFilterFactory");

        if(isJwtValid(exchange)){
            //make some additions to the request like its identity
            return chain.filter(exchange);
        }

        //TODO investigate why after returning Mono on the previous filter it gets also to here
        //TODO investigate why this is not in the body of the response
        return onError(exchange, HttpStatus.UNAUTHORIZED, "Jwt token is not valid");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        exchange
                .getRequest()
                .mutate()
                .header("token-X", String.valueOf(token))
                .build();
    }

    public static Mono<Void> onError(ServerWebExchange exchange, HttpStatus status, String message) {
        log.info("Must be printing error from jwtFilter");

        exchange.getResponse()
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

    public boolean isJwtValid(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String authorizationHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix

            boolean isJwtValid = isJwtValid(token);
            log.info("Provided JWT token is valid: {}",isJwtValid);

            return isJwtValid;
        }

        log.warn("No Bearer token found");
        return false;
    }

    private boolean isJwtValid(String token) {
        return token.matches("^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_.+/=]*$");
    }

//    UserDTO userDTO = restTemplate.postForObject("lb://AUTH-SERVICE/validate", authRequest, UserDTO.class);
//            if (jwtUtil.isInvalid(token)) {
//        return this.onError(exchange, "Authorization header is invalid");
//    }
}
