package com.nhnacademy.minidoorayfe.config;

import com.nhnacademy.minidoorayfe.properties.ApiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Bean("gatewayRestClient")
    public RestClient accountRestClient(ApiProperties apiProperties) { // TODO ApiPropertis 주입함

        JsonMapper restClientMapper = JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();

        return RestClient.builder()
                .baseUrl(apiProperties.getGatewayUrl()) // TODO 추가함
                .requestFactory(requestFactory())
                .defaultHeader("Content-Type", "application/json")

                .configureMessageConverters(builder ->
                        builder.withJsonConverter(new JacksonJsonHttpMessageConverter(restClientMapper))
                )

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

