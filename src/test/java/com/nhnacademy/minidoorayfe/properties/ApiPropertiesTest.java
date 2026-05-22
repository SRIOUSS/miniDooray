package com.nhnacademy.minidoorayfe.properties;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApiPropertiesTest {

    @Test
    void propertiesTest() {
        ApiProperties apiProperties = new ApiProperties();
        apiProperties.setGatewayUrl("http://localhost:8000");
        assertThat(apiProperties.getGatewayUrl()).isEqualTo("http://localhost:8000");
    }
}