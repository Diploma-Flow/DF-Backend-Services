package org.simo.dms.diplomamanagementservice.config;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.simo.auth.context.provider.RequestContext;
import org.simo.auth.context.provider.RequestContextHolder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

/**
 * Author: Simeon Popov
 * Date of creation: 6/19/2024
 */

@Configuration
@RequiredArgsConstructor
public class DiplomaManagementConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalanced() {
        return RestClient.builder();
    }
}
