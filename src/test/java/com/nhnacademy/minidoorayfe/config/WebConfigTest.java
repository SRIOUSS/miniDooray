package com.nhnacademy.minidoorayfe.config;

import com.nhnacademy.minidoorayfe.resolver.SessionArgumentResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WebConfigTest {

    @Mock
    JsonMapper jsonMapper;

    @InjectMocks
    WebConfig webConfig;

    @Test
    void addArgumentResolver() {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
        webConfig.addArgumentResolvers(resolvers);
        assertThat(resolvers).hasSize(1);
        assertThat(resolvers.getFirst()).isInstanceOf(SessionArgumentResolver.class);
    }
}