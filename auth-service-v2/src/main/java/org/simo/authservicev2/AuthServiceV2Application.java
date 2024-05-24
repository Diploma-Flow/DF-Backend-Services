package org.simo.authservicev2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class AuthServiceV2Application {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceV2Application.class, args);
    }

}
