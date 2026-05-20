package com.nhnacademy.minidoorayfe.config;

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

    @Bean("gatewayRestClient")
    public RestClient accountRestClient() {

        return RestClient.builder()
                .requestFactory(requestFactory())
                .defaultHeader("Content-Type", "application/json")

                .defaultStatusHandler(
                        status -> status.equals(HttpStatus.NOT_FOUND),
                        ((request, response) -> {
                            throw new RestClientException("존재하지 않는 리소스");
                        })
                )
                .defaultStatusHandler(
                        HttpStatusCode::isError,
                        (req, res) -> {
                            throw new RestClientException("Gateway API 호출 실패: " + res.getStatusCode());
                        })
                .build();
    }

    private SimpleClientHttpRequestFactory requestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(5));
        return factory;
    }

}

