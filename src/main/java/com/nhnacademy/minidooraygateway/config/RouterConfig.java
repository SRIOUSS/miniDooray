package com.nhnacademy.minidooraygateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouterConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/account-api/**").uri("http://localhost:8081"))
                .route(p -> p.path("/task-api/**").uri("http://localhost:8082"))
                .route(p -> p.path("/auth-api/**").uri("http://localhost:8083"))
                .build();
    }
}
