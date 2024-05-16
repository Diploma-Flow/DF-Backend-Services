package org.simo.defaultgateway.config;

import lombok.RequiredArgsConstructor;
import org.simo.defaultgateway.exception.HeaderValidationException;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Author: Simeon Popov
 * Date of creation: 9.1.2024 г..
 */

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
