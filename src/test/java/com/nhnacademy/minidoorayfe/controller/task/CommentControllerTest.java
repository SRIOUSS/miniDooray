package com.nhnacademy.minidoorayfe.controller.task;

import com.nhnacademy.minidoorayfe.api.TaskApiClient;
import com.nhnacademy.minidoorayfe.auth.BlackList;
import com.nhnacademy.minidoorayfe.dto.auth.SessionAccountDto;
import com.nhnacademy.minidoorayfe.dto.comment.CommentRequestDto;
import com.nhnacademy.minidoorayfe.resolver.SessionArgumentResolver;
import com.nhnacademy.minidoorayfe.resolver.SessionConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
@Import(CommentControllerTest.TestConfig.class)
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TaskApiClient taskApiClient;

    @MockitoBean
    BlackList blackList;

    MockHttpSession session;

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

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute(SessionConstants.SESSION_KEY, new SessionAccountDto(1L, "testUser"));
    }

    @Test
    @WithMockUser
    void createComment_redirectsToTaskDetail() throws Exception {
        mockMvc.perform(post("/projects/10/tasks/20/comments")
                        .session(session)
                        .param("content", "new comment")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/10/tasks/20"));

        verify(taskApiClient).createComment(eq(20L), eq(1L), any(CommentRequestDto.class));
    }

    @Test
    @WithMockUser
    void updateComment_redirectsToTaskDetail() throws Exception {
        mockMvc.perform(put("/projects/10/tasks/20/comments/5")
                        .session(session)
                        .param("content", "updated comment")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/10/tasks/20"));

        verify(taskApiClient).updateComment(eq(20L), eq(1L), eq(5L), any(CommentRequestDto.class));
    }

    @Test
    @WithMockUser
    void deleteComment_redirectsToTaskDetail() throws Exception {
        mockMvc.perform(delete("/projects/10/tasks/20/comments/5")
                        .session(session)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/10/tasks/20"));

        verify(taskApiClient).deleteComment(20L, 1L, 5L);
    }
}