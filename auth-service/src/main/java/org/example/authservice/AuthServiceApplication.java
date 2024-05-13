package org.example.authservice;

import lombok.RequiredArgsConstructor;
import org.example.authservice.service.UserService;
import org.example.authservice.util.ServiceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableMongoRepositories
@RequiredArgsConstructor
public class AuthServiceApplication {
    private final UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Scheduled(fixedRateString = "${user-service.health-check.delay}")
    public void pingUserService() {
        userService.pingUserService();
    }
}
