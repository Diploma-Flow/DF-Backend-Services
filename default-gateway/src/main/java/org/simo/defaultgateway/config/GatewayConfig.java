package org.simo.defaultgateway.config;

import lombok.RequiredArgsConstructor;
import org.simo.defaultgateway.filters.CustomGlobalFilter;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Author: Simeon Popov
 * Date of creation: 9.1.2024 г..
 */

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    @Bean
    public GlobalFilter customFilter(CustomGlobalFilter customGlobalFilter) {
        return customGlobalFilter;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
