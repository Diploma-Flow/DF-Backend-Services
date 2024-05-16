package org.simo.defaultgateway.exception.util;

import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */
public class ApiExceptionFactory {
    public ApiException generateApiException(Exception e, HttpStatus httpStatus) {
        return ApiException.builder()
                .response(e.getMessage())
                .httpStatus(httpStatus)
                .timestamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();
    }

    public ApiException generateApiException(String responseMessage, HttpStatus httpStatus) {
        return ApiException.builder()
                .response(responseMessage)
                .httpStatus(httpStatus)
                .timestamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();
    }
}
