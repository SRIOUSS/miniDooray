package com.nhnacademy.minidooraytask.config;

import com.nhnacademy.minidooraytask.config.exception.UsernameNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(10));
        factory.setReadTimeout(Duration.ofSeconds(10));

        return RestClient.builder()
                .requestFactory(factory)
                .defaultHeader("Content-Type", "application/json")
                .defaultStatusHandler(
                        status -> status.equals(HttpStatus.NOT_FOUND),
                        ((request, response) -> { throw new UsernameNotFoundException("unexist user");})
                )
                .defaultStatusHandler(
                        HttpStatusCode::isError,
                        (req, res) -> {throw new RestClientException("API 호출 실패: " + res.getStatusCode());})
                .build();
    }
}