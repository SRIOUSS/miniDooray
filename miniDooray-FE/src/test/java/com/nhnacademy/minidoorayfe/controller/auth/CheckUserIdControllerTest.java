package com.nhnacademy.minidoorayfe.controller.auth;

import com.nhnacademy.minidoorayfe.api.AccountApiClient;
import com.nhnacademy.minidoorayfe.auth.BlackList;
import com.nhnacademy.minidoorayfe.dto.auth.AccountResponseDto;
import com.nhnacademy.minidoorayfe.resolver.SessionArgumentResolver;
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
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CheckUserIdController.class)
@Import(CheckUserIdControllerTest.TestConfig.class)
class CheckUserIdControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AccountApiClient accountApiClient;

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
    }

    @Test
    @WithMockUser
    void checkUserId_existingUser_returnsTrue() throws Exception {
        given(accountApiClient.findByUserId("existingUser")).willReturn(new AccountResponseDto());

        mockMvc.perform(get("/check-userId").param("userId", "existingUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true));
    }

    @Test
    @WithMockUser
    void checkUserId_nonExistentUser_returnsFalse() throws Exception {
        given(accountApiClient.findByUserId("unknownUser"))
                .willThrow(new UsernameNotFoundException("not found"));

        mockMvc.perform(get("/check-userId").param("userId", "unknownUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(false));
    }
}