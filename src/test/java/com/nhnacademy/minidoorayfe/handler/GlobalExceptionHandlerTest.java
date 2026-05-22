package com.nhnacademy.minidoorayfe.handler;

import com.nhnacademy.minidoorayfe.auth.BlackList;
import com.nhnacademy.minidoorayfe.exception.ApiServerException;
import com.nhnacademy.minidoorayfe.resolver.SessionArgumentResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GlobalExceptionHandler.class)
@Import(GlobalExceptionHandlerTest.TestConfig.class)
class GlobalExceptionHandlerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BlackList blackList;

    @TestConfiguration
    static class TestConfig implements WebMvcConfigurer {
        @Bean
        JsonMapper jsonMapper() {
            return JsonMapper.builder().build();
        }

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new SessionArgumentResolver(JsonMapper.builder().build()));
        }

        @RestController
        static class TestController {
            @GetMapping("/test/username-not-found")
            public void usernameNotFound() {
                throw new UsernameNotFoundException("not found");
            }

            @GetMapping("/test/api-server-error")
            public void apiServerError() {
                throw new ApiServerException("server error");
            }

            @GetMapping("/test/rest-client-error")
            public void restClientError() {
                throw new RestClientException("client error");
            }

            @GetMapping("/test/exception")
            public void exception() {
                throw new RuntimeException("unknown error");
            }
        }
    }

    @Test
    @WithMockUser
    @DisplayName("UsernameNotFoundException - 404")
    void handleUsernameNotFoundException() throws Exception {
        mockMvc.perform(get("/test/username-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(model().attribute("statusCode", 404));
    }

    @Test
    @WithMockUser
    @DisplayName("ApiServerException - 500")
    void handleApiServerException() throws Exception {
        mockMvc.perform(get("/test/api-server-error"))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error/5xx"))
                .andExpect(model().attribute("statusCode", 500));
    }

    @Test
    @WithMockUser
    @DisplayName("RestClientException - 400")
    void handleRestClientException() throws Exception {
        mockMvc.perform(get("/test/rest-client-error"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error/4xx"))
                .andExpect(model().attribute("statusCode", 400));
    }

    @Test
    @WithMockUser
    @DisplayName("Exception - 500")
    void handleException() throws Exception {
        mockMvc.perform(get("/test/exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error/5xx"))
                .andExpect(model().attribute("statusCode", 500));
    }
}
