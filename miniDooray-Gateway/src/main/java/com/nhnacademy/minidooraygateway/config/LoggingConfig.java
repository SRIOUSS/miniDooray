package com.nhnacademy.minidooraygateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Slf4j
@Configuration
public class LoggingConfig {

    @Bean
    public GlobalFilter loggingFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            log.info("[Gateway] {} {} from {}",
                    request.getMethod(),
                    request.getURI(),
                    request.getRemoteAddress()
            );

            return chain.filter(exchange).doOnSuccess(v ->
                    log.info("[Gateway] Response: {}",
                            exchange.getResponse().getStatusCode()
                    )
            );
        };
    }
}