package com.nhnacademy.minidoorayfe.controller.task;

import com.nhnacademy.minidoorayfe.api.TaskApiClient;
import com.nhnacademy.minidoorayfe.auth.BlackList;
import com.nhnacademy.minidoorayfe.dto.auth.SessionAccountDto;
import com.nhnacademy.minidoorayfe.dto.task.TaskInfoListDto;
import com.nhnacademy.minidoorayfe.dto.task.TaskRequestDto;
import com.nhnacademy.minidoorayfe.dto.task.TaskResponseDto;
import com.nhnacademy.minidoorayfe.dto.task.TaskViewDto;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@Import(TaskControllerTest.TestConfig.class)
class TaskControllerTest {

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

    private TaskViewDto buildTaskViewDto() {
        TaskViewDto taskViewDto = new TaskViewDto();
        taskViewDto.setTaskResponseDto(new TaskResponseDto());
        return taskViewDto;
    }

    @Test
    @WithMockUser
    void getTasks_returnsListView() throws Exception {
        given(taskApiClient.getTasks(10L, 1L)).willReturn(new TaskInfoListDto());

        mockMvc.perform(get("/projects/10/tasks").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("task/list"))
                .andExpect(model().attribute("projectId", 10L))
                .andExpect(model().attributeExists("tasks"));

        verify(taskApiClient).getTasks(10L, 1L);
    }

    @Test
    @WithMockUser
    void getTask_returnsDetailView() throws Exception {
        TaskViewDto taskViewDto = buildTaskViewDto();
        given(taskApiClient.getTask(10L, 20L, 1L)).willReturn(taskViewDto);
        given(taskApiClient.getTasks(10L, 1L)).willReturn(new TaskInfoListDto());

        mockMvc.perform(get("/projects/10/tasks/20").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("task/detail"))
                .andExpect(model().attribute("projectId", 10L))
                .andExpect(model().attribute("taskId", 20L))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attributeExists("allTasks"))
                .andExpect(model().attributeExists("milestoneRequestDto"))
                .andExpect(model().attributeExists("commentRequestDto"));

        verify(taskApiClient).getTask(10L, 20L, 1L);
        verify(taskApiClient).getTasks(10L, 1L);
    }

    @Test
    @WithMockUser
    void createTaskForm_returnsFormView() throws Exception {
        mockMvc.perform(get("/projects/10/tasks/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("task/form"))
                .andExpect(model().attribute("projectId", 10L))
                .andExpect(model().attributeExists("taskRequestDto"));
    }

    @Test
    @WithMockUser
    void createTask_redirectsToTaskList() throws Exception {
        mockMvc.perform(post("/projects/10/tasks")
                        .session(session)
                        .param("title", "New Task")
                        .param("content", "content")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/10/tasks"));

        verify(taskApiClient).createTask(eq(10L), eq(1L), any(TaskRequestDto.class));
    }

    @Test
    @WithMockUser
    void updateTaskForm_returnsEditView() throws Exception {
        given(taskApiClient.getTask(10L, 20L, 1L)).willReturn(buildTaskViewDto());

        mockMvc.perform(get("/projects/10/tasks/20/edit").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("task/edit"))
                .andExpect(model().attribute("projectId", 10L))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attributeExists("taskRequestDto"))
                .andExpect(model().attributeExists("tagNames"));

        verify(taskApiClient).getTask(10L, 20L, 1L);
    }

    @Test
    @WithMockUser
    void updateTask_redirectsToTaskDetail() throws Exception {
        mockMvc.perform(put("/projects/10/tasks/20")
                        .session(session)
                        .param("title", "Updated Task")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/10/tasks/20"));

        verify(taskApiClient).updateTask(eq(10L), eq(20L), eq(1L), any(TaskRequestDto.class));
    }

    @Test
    @WithMockUser
    void deleteTask_redirectsToTaskList() throws Exception {
        mockMvc.perform(delete("/projects/10/tasks/20")
                        .session(session)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/10/tasks"));

        verify(taskApiClient).deleteTask(10L, 20L, 1L);
    }
}