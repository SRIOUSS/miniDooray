package com.nhnacademy.minidoorayfe.controller.task;

import com.nhnacademy.minidoorayfe.api.TaskApiClient;
import com.nhnacademy.minidoorayfe.auth.BlackList;
import com.nhnacademy.minidoorayfe.dto.auth.SessionAccountDto;
import com.nhnacademy.minidoorayfe.dto.comment.CommentListDto;
import com.nhnacademy.minidoorayfe.dto.task.TaskInfoListDto;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MyPageController.class)
@Import(MyPageControllerTest.TestConfig.class)
class MyPageControllerTest {

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
    void getMyPage_returnsIndexView() throws Exception {
        given(taskApiClient.getMyTasks(1L)).willReturn(new TaskInfoListDto());
        given(taskApiClient.getMyComments(1L)).willReturn(new CommentListDto());

        mockMvc.perform(get("/mypage").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("mypage/index"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attributeExists("comments"));

        verify(taskApiClient).getMyTasks(1L);
        verify(taskApiClient).getMyComments(1L);
    }

    @Test
    @WithMockUser
    void getMyTasks_returnsTasksView() throws Exception {
        given(taskApiClient.getMyTasks(1L)).willReturn(new TaskInfoListDto());

        mockMvc.perform(get("/mypage/tasks").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("mypage/tasks"))
                .andExpect(model().attributeExists("tasks"));

        verify(taskApiClient).getMyTasks(1L);
    }

    @Test
    @WithMockUser
    void getMyComments_returnsCommentsView() throws Exception {
        given(taskApiClient.getMyComments(1L)).willReturn(new CommentListDto());

        mockMvc.perform(get("/mypage/comments").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("mypage/comments"))
                .andExpect(model().attributeExists("comments"));

        verify(taskApiClient).getMyComments(1L);
    }
}