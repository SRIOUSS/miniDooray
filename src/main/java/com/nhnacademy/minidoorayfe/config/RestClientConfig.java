package com.nhnacademy.minidoorayfe.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Base64;

@Configuration
public class RestClientConfig {

    @Bean("accountRestClient")
    public RestClient accountRestClient() {

        return RestClient.builder()
                .requestFactory(requestFactory())
                .defaultHeader("Content-Type", "application/json")

                .defaultStatusHandler(
                        status -> status.equals(HttpStatus.NOT_FOUND),
                        ((request, response) -> {
                            throw new UsernameNotFoundException("unexist user");
                        })
                )
                .defaultStatusHandler(
                        HttpStatusCode::isError,
                        (req, res) -> {
                            throw new RestClientException("Account API 호출 실패: " + res.getStatusCode());
                        })
                .build();
    }

    @Bean("taskRestClient")
    public RestClient taskRestClient() {

        return RestClient.builder()
                .requestFactory(requestFactory())
                .defaultHeader("Content-Type", "application/json")

                .defaultStatusHandler(
                        status -> status.equals(HttpStatus.NOT_FOUND),
                        (req, res) -> {
                            throw new RestClientException("존재하지 않는 리소스");
                        }
                )
                .defaultStatusHandler(
                        HttpStatusCode::isError,
                        (req, res) -> {
                            throw new RestClientException("Task API 호출 실패: " + res.getStatusCode());
                        }
                )
                .build();
    }

    private SimpleClientHttpRequestFactory requestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(5));
        return factory;
    }

}

