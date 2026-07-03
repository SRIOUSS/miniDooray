package com.nhnacademy.minidoorayfe.config;

import com.nhnacademy.minidoorayfe.properties.ApiProperties;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

class RestClientConfigTest {

    RestClientConfig clientConfig = new RestClientConfig();

    @Test
    void createBeanTest() {
        ApiProperties apiProperties = new ApiProperties();
        apiProperties.setGatewayUrl("http://localhost:8000");
        RestClient restClient = clientConfig.accountRestClient(apiProperties);
        assertThat(restClient).isNotNull();
    }
}