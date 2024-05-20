package org.example.authservice.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Author: Simeon Popov
 * Date of creation: 5/13/2024
 */

@Data
@Component
public class ServiceProperties {

    @Value("${user-service.health-check.url}")
    private String userServiceHealthCheckUrl;

    @Value("${user-service.auth.url.login}")
    private String userServiceLoginUrl;

    @Value("${user-service.auth.url.register}")
    private String userServiceRegisterUrl;
}
