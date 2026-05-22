package com.nhnacademy.minidoorayfe.controller.auth;

import com.nhnacademy.minidoorayfe.api.AccountApiClient;
import com.nhnacademy.minidoorayfe.auth.BlackList;
import com.nhnacademy.minidoorayfe.dto.auth.SignFormDto;
import com.nhnacademy.minidoorayfe.resolver.SessionArgumentResolver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SignUpController.class)
@Import(SignUpControllerTest.TestConfig.class)
class SignUpControllerTest {

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
    void signUpPage_returnsSignupView() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/signup"))
                .andExpect(model().attributeExists("signUpFormDto"));
    }

    @Test
    @WithMockUser
    void signUp_redirectsToLogin() throws Exception {
        mockMvc.perform(post("/signup")
                        .param("userId", "newUser")
                        .param("userPassword", "pass1234")
                        .param("userName", "테스트")
                        .param("userEmail", "test@test.com")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(accountApiClient).register(any(SignFormDto.class));
    }
}