package org.simo.defaultgateway.exception.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Simeon Popov
 * Date of creation: 23.1.2024 г.
 */

@Configuration
public class ExceptionConfig {
    @Bean
    public ApiExceptionFactory apiExceptionGenerator() {
        return new ApiExceptionFactory();
    }
}
