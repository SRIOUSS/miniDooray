package com.nhnacademy.minidoorayfe.controller.task;

import com.nhnacademy.minidoorayfe.api.TaskApiClient;
import com.nhnacademy.minidoorayfe.auth.BlackList;
import com.nhnacademy.minidoorayfe.dto.auth.SessionAccountDto;
import com.nhnacademy.minidoorayfe.dto.milestone.MilestoneRequestDto;
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

@WebMvcTest(MilestoneController.class)
@Import(MilestoneControllerTest.TestConfig.class)
class MilestoneControllerTest {

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
    void createMilestone_redirectsToTaskDetail() throws Exception {
        mockMvc.perform(post("/projects/10/tasks/20/milestones")
                        .session(session)
                        .param("title", "M1")
                        .param("description", "desc")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/10/tasks/20"));

        verify(taskApiClient).createMilestone(eq(20L), eq(1L), any(MilestoneRequestDto.class));
    }

    @Test
    @WithMockUser
    void createMilestone_withDueDate_redirectsToTaskDetail() throws Exception {
        mockMvc.perform(post("/projects/10/tasks/20/milestones")
                        .session(session)
                        .param("title", "M1")
                        .param("description", "desc")
                        .param("dueDateDate", "2026-06-01")
                        .param("dueDateTime", "09:00")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/10/tasks/20"));

        verify(taskApiClient).createMilestone(eq(20L), eq(1L), any(MilestoneRequestDto.class));
    }

    @Test
    @WithMockUser
    void updateMilestone_redirectsToTaskDetail() throws Exception {
        mockMvc.perform(put("/projects/10/tasks/20/milestones")
                        .session(session)
                        .param("title", "Updated M1")
                        .param("description", "desc")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/10/tasks/20"));

        verify(taskApiClient).updateMilestone(eq(20L), eq(1L), any(MilestoneRequestDto.class));
    }

    @Test
    @WithMockUser
    void updateMilestone_withDueDateOnly_usesDefaultTime() throws Exception {
        mockMvc.perform(put("/projects/10/tasks/20/milestones")
                        .session(session)
                        .param("title", "Updated M1")
                        .param("description", "desc")
                        .param("dueDateDate", "2026-06-15")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/10/tasks/20"));

        verify(taskApiClient).updateMilestone(eq(20L), eq(1L), any(MilestoneRequestDto.class));
    }

    @Test
    @WithMockUser
    void deleteMilestone_redirectsToTaskDetail() throws Exception {
        mockMvc.perform(delete("/projects/10/tasks/20/milestones")
                        .session(session)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/10/tasks/20"));

        verify(taskApiClient).deleteMilestone(20L, 1L);
    }
}