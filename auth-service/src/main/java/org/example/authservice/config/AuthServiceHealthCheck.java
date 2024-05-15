package org.example.authservice.config;

import lombok.RequiredArgsConstructor;
import org.example.authservice.service.AuthService;
import org.example.authservice.service.UserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Author: Simeon Popov
 * Date of creation: 5/16/2024
 */

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "user-service.health-check.enabled", havingValue = "true")
@RequiredArgsConstructor
public class AuthServiceHealthCheck {

    private final UserService userService;

    @Scheduled(fixedRateString = "${user-service.health-check.delay}")
    public void pingUserService() {
        userService.pingUserService();
    }
}
