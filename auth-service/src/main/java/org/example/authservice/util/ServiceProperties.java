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
    private String USER_SERVICE_HEALTH_CHECK_URL;

    @Value("${user-service.health-check.delay}")
    private long USER_SERVICE_HEALTH_CHECK_PING_INTERVAL;

    @Value("${user-service.url.login}")
    private String USER_SERVICE_LOGIN_URL;

    @Value("${user-service.url.register}")
    private String USER_SERVICE_REGISTER_URL;
}
