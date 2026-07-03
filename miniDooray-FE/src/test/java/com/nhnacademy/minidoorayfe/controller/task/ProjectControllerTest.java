package com.nhnacademy.minidoorayfe.controller.task;

import com.nhnacademy.minidoorayfe.api.TaskApiClient;
import com.nhnacademy.minidoorayfe.auth.BlackList;
import com.nhnacademy.minidoorayfe.dto.auth.SessionAccountDto;
import com.nhnacademy.minidoorayfe.dto.member.MemberInfoListDto;
import com.nhnacademy.minidoorayfe.dto.member.MemberRequestDto;
import com.nhnacademy.minidoorayfe.dto.project.ProjectRequestDto;
import com.nhnacademy.minidoorayfe.dto.project.ProjectViewDto;
import com.nhnacademy.minidoorayfe.dto.task.TaskInfoListDto;
import com.nhnacademy.minidoorayfe.resolver.SessionArgumentResolver;
import com.nhnacademy.minidoorayfe.resolver.SessionConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
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

@WebMvcTest(ProjectController.class)
@Import(ProjectControllerTest.TestConfig.class)
class ProjectControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TaskApiClient taskApiClient;

    // IpBlackListFilter → BlackList → RedisTemplate 의존 체인을 끊기 위해 mock
    @MockitoBean
    BlackList blackList;

    MockHttpSession session;

    @TestConfiguration
    static class TestConfig implements WebMvcConfigurer {

        // WebConfig 가 @WebMvcTest 에 포함될 때 JsonMapper 의존성 충족
        @Bean
        JsonMapper jsonMapper() {
            return JsonMapper.builder().build();
        }

        // WebConfig 가 로드되지 않는 경우를 대비해 직접 resolver 등록
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
    void getProjects_returnsListView() throws Exception {
        given(taskApiClient.getProjects(1L)).willReturn(new ProjectViewDto());

        mockMvc.perform(get("/projects").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("project/list"))
                .andExpect(model().attributeExists("projectView"));

        verify(taskApiClient).getProjects(1L);
    }

    @Test
    @WithMockUser
    void createProjectForm_returnsFormView() throws Exception {
        mockMvc.perform(get("/projects/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("project/form"))
                .andExpect(model().attributeExists("projectRequestDto"));
    }

    @Test
    @WithMockUser
    void createProject_redirectsToProjects() throws Exception {
        mockMvc.perform(post("/projects")
                        .session(session)
                        .param("title", "New Project")
                        .param("description", "desc")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));

        verify(taskApiClient).createProject(any(ProjectRequestDto.class), eq(1L));
    }

    @Test
    @WithMockUser
    void getProject_returnsDetailView() throws Exception {
        given(taskApiClient.getTasks(10L, 1L)).willReturn(new TaskInfoListDto());
        given(taskApiClient.getMembers(10L, 1L)).willReturn(new MemberInfoListDto());

        mockMvc.perform(get("/projects/10").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("project/detail"))
                .andExpect(model().attribute("projectId", 10L))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attributeExists("members"));

        verify(taskApiClient).getTasks(10L, 1L);
        verify(taskApiClient).getMembers(10L, 1L);
    }

    @Test
    @WithMockUser
    void updateProjectForm_returnsEditView() throws Exception {
        given(taskApiClient.getTasks(10L, 1L)).willReturn(new TaskInfoListDto());

        mockMvc.perform(get("/projects/10/edit").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("project/edit"))
                .andExpect(model().attribute("projectId", 10L))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attributeExists("projectRequestDto"));

        verify(taskApiClient).getTasks(10L, 1L);
    }

    @Test
    @WithMockUser
    void updateProject_redirectsToProjectDetail() throws Exception {
        mockMvc.perform(put("/projects/10")
                        .session(session)
                        .param("title", "Updated")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/10"));

        verify(taskApiClient).updateProject(eq(10L), any(ProjectRequestDto.class), eq(1L));
    }

    @Test
    @WithMockUser
    void deleteProject_redirectsToProjects() throws Exception {
        mockMvc.perform(delete("/projects/10")
                        .session(session)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));

        verify(taskApiClient).deleteProject(10L, 1L);
    }

    @Test
    @WithMockUser
    void addMemberForm_returnsMemberFormView() throws Exception {
        mockMvc.perform(get("/projects/10/members/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("project/member-form"))
                .andExpect(model().attribute("projectId", 10L))
                .andExpect(model().attributeExists("memberRequestDto"));
    }

    @Test
    @WithMockUser
    void addMember_redirectsToProjectDetail() throws Exception {
        mockMvc.perform(post("/projects/10/members")
                        .session(session)
                        .param("userId", "newUser")
                        .param("accountId", "2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/10"));

        verify(taskApiClient).addMember(eq(10L), eq(1L), any(MemberRequestDto.class));
    }

    @Test
    @WithMockUser
    void updateMemberAuth_redirectsToProjectDetail() throws Exception {
        mockMvc.perform(put("/projects/10/members/5")
                        .session(session)
                        .param("auth", "MEMBER")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/10"));

        verify(taskApiClient).updateMemberAuth(eq(10L), eq(5L), any(MemberRequestDto.class), eq(1L));
    }

    @Test
    @WithMockUser
    void deleteMember_redirectsToProjectDetail() throws Exception {
        mockMvc.perform(delete("/projects/10/members/5")
                        .session(session)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/10"));

        verify(taskApiClient).deleteMember(10L, 1L, 5L);
    }
}