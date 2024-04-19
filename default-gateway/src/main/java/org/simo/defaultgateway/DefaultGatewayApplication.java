package org.simo.defaultgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DefaultGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(DefaultGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/auth/**")
                        .uri("lb://AUTH-SERVICE"))

                .route("message-service", r -> r.path("/message/**")
                        .uri("lb://MESSAGE-SERVICE"))

                .route("user-service", r -> r.path("/user/**")
                        .uri("lb://USER-SERVICE"))

                .route("eureka-dashboard", r -> r.path("/eureka/main")
                        .filters(f -> f.rewritePath("/eureka/main", "/"))
                        .uri("http://localhost:8761"))
                .route("eureka-dashboard-static", r -> r.path("/eureka/**")
                        .uri("http://localhost:8761"))
                .build();
    }
}
