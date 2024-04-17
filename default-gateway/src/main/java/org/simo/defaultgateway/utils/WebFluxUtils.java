package org.simo.defaultgateway.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Author: Simeon Popov
 * Date of creation: 17.4.2024 г.
 */

@Log4j2
public class WebFluxUtils {
    public static Mono<Void> onError(ServerWebExchange exchange, HttpStatus status, String message) {
        exchange.getResponse()
                .setStatusCode(status);

        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange
                .getResponse()
                .bufferFactory()
                .wrap(bytes);

        return exchange
                .getResponse()
                .writeWith(Mono.just(buffer));
    }
}
