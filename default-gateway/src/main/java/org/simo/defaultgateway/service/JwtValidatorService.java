package org.simo.defaultgateway.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.simo.defaultgateway.utils.WebFluxUtils.onError;

/**
 * Author: Simeon Popov
 * Date of creation: 17.4.2024 г.
 */

@Log4j2
@Service
public class JwtValidatorService {

    public boolean isAuthMissing(ServerHttpRequest request) {
        return !request
                .getHeaders()
                .containsKey("Authorization");
    }

    public boolean isAuthHeaderBlank(ServerHttpRequest request) {
        return getAuthHeader(request).isBlank();
    }

    public String getAuthHeader(ServerHttpRequest request) {
        return request
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);
    }

    public boolean isBearerMissing(ServerHttpRequest request) {
        return !getAuthHeader(request).startsWith("Bearer ");
    }

    public boolean isJwtFormatValid(ServerHttpRequest request) {
        String token = getAuthHeader(request).substring(7);
        return !token.isBlank() && token.matches("^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_.+/=]*$");
    }

    public boolean isJwtValid(ServerHttpRequest request) {
        return true;
    }
}
