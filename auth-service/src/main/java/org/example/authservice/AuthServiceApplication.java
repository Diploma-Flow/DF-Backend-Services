package org.example.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class AuthServiceApplication {

    //TODO session activity chron job every 30 minutes to delete stale logins
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
