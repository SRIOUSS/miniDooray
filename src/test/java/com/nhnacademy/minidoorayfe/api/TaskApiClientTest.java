package com.nhnacademy.minidoorayfe.api;

import com.nhnacademy.minidoorayfe.dto.comment.CommentListDto;
import com.nhnacademy.minidoorayfe.dto.comment.CommentRequestDto;
import com.nhnacademy.minidoorayfe.dto.member.MemberInfoListDto;
import com.nhnacademy.minidoorayfe.dto.member.MemberRequestDto;
import com.nhnacademy.minidoorayfe.dto.member.MembersAuth;
import com.nhnacademy.minidoorayfe.dto.milestone.MilestoneRequestDto;
import com.nhnacademy.minidoorayfe.dto.milestone.MilestoneStatus;
import com.nhnacademy.minidoorayfe.dto.project.ProjectRequestDto;
import com.nhnacademy.minidoorayfe.dto.project.ProjectStatus;
import com.nhnacademy.minidoorayfe.dto.project.ProjectViewDto;
import com.nhnacademy.minidoorayfe.dto.task.TaskInfoListDto;
import com.nhnacademy.minidoorayfe.dto.task.TaskRequestDto;
import com.nhnacademy.minidoorayfe.dto.task.TaskViewDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(TaskApiClient.class)
class TaskApiClientTest {

    @Autowired
    MockRestServiceServer server;

    @Autowired
    TaskApiClient taskApiClient;

    private static final String PREFIX = "http://localhost:8000";

    @TestConfiguration
    static class TestConfig {
        @Bean
        RestClient gatewayRestClient(RestClient.Builder builder) {
            return builder.baseUrl("http://localhost:8000").build();
        }

        @Bean
        TaskApiClient taskApiClient(@Qualifier("gatewayRestClient") RestClient restClient) {
            return new TaskApiClient(restClient);
        }
    }

    // ---------------------- Project ----------------------

    @Test
    void getProjects() {
        String json = """
                {
                    "projectInfoDtoList": [
                        {
                            "id": 1,
                            "title": "테스트제목",
                            "status": "ACTIVE",
                            "taskStatusList": []
                        }
                    ],
                    "taskInfoDtoList": []
                }
                """;

        server.expect(requestTo(PREFIX + "/task-api/projects"))
                .andExpect(header("X-Account-Id", "1"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        ProjectViewDto result = taskApiClient.getProjects(1L);
        assertThat(result).isNotNull();
        assertThat(result.getProjectInfoDtoList()).hasSize(1);
        assertThat(result.getProjectInfoDtoList().getFirst().getId()).isEqualTo(1);
        assertThat(result.getProjectInfoDtoList().getFirst().getTitle()).isEqualTo("테스트제목");
    }

    @Test
    void createProject() {
        String json = """
                {
                    "title": "테스트",
                    "description": "설명",
                    "status": "ACTIVE"
                }
                """;
        server.expect(requestTo(PREFIX + "/task-api/projects"))
                .andExpect(method(POST))
                .andExpect(header("X-Account-Id", "1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json))
                .andRespond(withSuccess());

        ProjectRequestDto dto = new ProjectRequestDto();
        dto.setTitle("테스트");
        dto.setDescription("설명");
        dto.setStatus(ProjectStatus.ACTIVE);

        taskApiClient.createProject(dto, 1L);
        server.verify();
    }

    @Test
    void updateProject() {
        String json = """
                {
                    "title": "테스트",
                    "description": "설명",
                    "status": "ACTIVE"
                }
                """;
        server.expect(requestTo(PREFIX + "/task-api/projects/1"))
                .andExpect(method(PUT))
                .andExpect(header("X-Account-Id", "1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json))
                .andRespond(withSuccess());

        ProjectRequestDto dto = new ProjectRequestDto();
        dto.setTitle("테스트");
        dto.setDescription("설명");
        dto.setStatus(ProjectStatus.ACTIVE);

        taskApiClient.updateProject(1L, dto, 1L);
        server.verify();
    }

    @Test
    void deleteProject() {
        server.expect(requestTo(PREFIX + "/task-api/projects/1"))
                .andExpect(method(DELETE))
                .andExpect(header("X-Account-Id", "1"))
                .andRespond(withSuccess());

        taskApiClient.deleteProject(1L, 1L);
        server.verify();
    }

    // ---------------------- Member ----------------------

    @Test
    void getMembers() {
        String json = """
                {
                    "memberInfoDtoList": [
                        {
                            "accountId": 1,
                            "memberId": 1,
                            "userId": "testUser",
                            "auth": "MEMBER",
                            "joinedAt": "2026-05-22T00:00:00"
                        }
                    ]
                }
                """;

        server.expect(requestTo(PREFIX + "/task-api/projects/1/members"))
                .andExpect(header("X-Account-Id", "1"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        MemberInfoListDto result = taskApiClient.getMembers(1L, 1L);
        assertThat(result).isNotNull();
        assertThat(result.getMemberInfoDtoList().getFirst().getAccountId()).isEqualTo(1);
        assertThat(result.getMemberInfoDtoList().getFirst().getMemberId()).isEqualTo(1);
        assertThat(result.getMemberInfoDtoList().getFirst().getUserId()).isEqualTo("testUser");
    }

    @Test
    void addMember() {
        String json = """
                {
                    "accountId": 1,
                    "userId": "유저아이디",
                    "auth": "MEMBER"
                }
                """;

        server.expect(requestTo(PREFIX + "/task-api/projects/1/members"))
                .andExpect(method(POST))
                .andExpect(header("X-Account-Id", "1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json))
                .andRespond(withSuccess());

        MemberRequestDto dto = new MemberRequestDto();
        dto.setAccountId(1);
        dto.setUserId("유저아이디");
        dto.setAuth(MembersAuth.MEMBER);

        taskApiClient.addMember(1L, 1L, dto);
        server.verify();
    }

    @Test
    void updateMemberAuth() {
        String json = """
                {
                    "accountId": 1,
                    "userId": "유저아이디",
                    "auth": "MEMBER"
                }
                """;

        server.expect(requestTo(PREFIX + "/task-api/projects/1/members/1"))
                .andExpect(method(PUT))
                .andExpect(header("X-Account-Id", "1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json))
                .andRespond(withSuccess());

        MemberRequestDto dto = new MemberRequestDto();
        dto.setAccountId(1);
        dto.setUserId("유저아이디");
        dto.setAuth(MembersAuth.MEMBER);

        taskApiClient.updateMemberAuth(1L, 1L, dto, 1L);
        server.verify();
    }

    @Test
    void deleteMember() {
        server.expect(requestTo(PREFIX + "/task-api/projects/1/members/1"))
                .andExpect(method(DELETE))
                .andExpect(header("X-Account-Id", "1"))
                .andRespond(withSuccess());

        taskApiClient.deleteMember(1L, 1L, 1L);
        server.verify();
    }

    // ---------------------- Task ----------------------

    @Test
    void getTasks() {
        String json = """
                {
                    "taskInfoDtoList": [
                        {
                            "id": 1,
                            "title": "제목",
                            "status": "PLANNED"
                        }
                    ]
                }
                """;

        server.expect(requestTo(PREFIX + "/task-api/projects/1/tasks"))
                .andExpect(header("X-Account-Id", "1"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        TaskInfoListDto result = taskApiClient.getTasks(1L, 1L);
        assertThat(result).isNotNull();
        assertThat(result.getTaskInfoDtoList().getFirst().getId()).isEqualTo(1);
        assertThat(result.getTaskInfoDtoList().getFirst().getTitle()).isEqualTo("제목");
    }

    @Test
    void getTask() {
        String json = """
                {
                    "taskResponseDto": {
                        "taskId": 1,
                        "title": "제목",
                        "content": "내용",
                        "createdAt": "2026-05-22T00:00:00",
                        "updatedAt": "2026-05-22T00:00:00",
                        "tagResponseDtoList": [],
                        "milestoneResponseDto": null
                    },
                    "taskInfoListDto": {
                        "taskInfoDtoList": []
                    },
                    "projectInfoDto": {
                        "id": 1,
                        "title": "제목",
                        "status": "ACTIVE",
                        "taskStatusList": []
                    },
                    "commentResponseDtoList": []
                }
                """;

        server.expect(requestTo(PREFIX + "/task-api/projects/1/tasks/1"))
                .andExpect(header("X-Account-Id", "1"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        TaskViewDto result = taskApiClient.getTask(1L, 1L, 1L);
        assertThat(result).isNotNull();
        assertThat(result.getTaskResponseDto().getTaskId()).isEqualTo(1);
        assertThat(result.getTaskResponseDto().getTitle()).isEqualTo("제목");
    }

    @Test
    void createTask() {
        String json = """
                {
                    "title": "제목",
                    "content": "내용",
                    "tagNameList": []
                }
                """;
        server.expect(requestTo(PREFIX + "/task-api/projects/1/tasks"))
                .andExpect(method(POST))
                .andExpect(header("X-Account-Id", "1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json))
                .andRespond(withSuccess());

        TaskRequestDto dto = new TaskRequestDto();
        dto.setTitle("제목");
        dto.setContent("내용");
        dto.setTagNameList(new ArrayList<>());

        taskApiClient.createTask(1L, 1L, dto);
        server.verify();
    }

    @Test
    void updateTask() {
        String json = """
                {
                    "title": "제목",
                    "content": "내용",
                    "tagNameList": []
                }
                """;
        server.expect(requestTo(PREFIX + "/task-api/projects/1/tasks/1"))
                .andExpect(method(PUT))
                .andExpect(header("X-Account-Id", "1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json))
                .andRespond(withSuccess());

        TaskRequestDto dto = new TaskRequestDto();
        dto.setTitle("제목");
        dto.setContent("내용");
        dto.setTagNameList(new ArrayList<>());

        taskApiClient.updateTask(1L, 1L, 1L, dto);
        server.verify();
    }

    @Test
    void deleteTask() {
        server.expect(requestTo(PREFIX + "/task-api/projects/1/tasks/1"))
                .andExpect(method(DELETE))
                .andExpect(header("X-Account-Id", "1"))
                .andRespond(withSuccess());

        taskApiClient.deleteTask(1L, 1L, 1L);
        server.verify();
    }

    @Test
    void getMyTasks() {
        String json = """
                {
                    "taskInfoDtoList": [
                        {
                            "id": 1,
                            "title": "제목",
                            "status": "PLANNED"
                        }
                    ]
                }
                """;

        server.expect(requestTo(PREFIX + "/task-api/mypage/tasks"))
                .andExpect(header("X-Account-Id", "1"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        TaskInfoListDto result = taskApiClient.getMyTasks(1L);
        assertThat(result).isNotNull();
        assertThat(result.getTaskInfoDtoList().getFirst().getId()).isEqualTo(1);
        assertThat(result.getTaskInfoDtoList().getFirst().getTitle()).isEqualTo("제목");
        assertThat(result.getTaskInfoDtoList().getFirst().getStatus()).isEqualTo(MilestoneStatus.PLANNED);
    }

    // ---------------------- Comment ----------------------

    @Test
    void createComment() {
        String json = """
                {
                    "content": "내용"
                }
                """;

        server.expect(requestTo(PREFIX + "/task-api/tasks/1/comments"))
                .andExpect(method(POST))
                .andExpect(header("X-Account-Id", "1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json))
                .andRespond(withSuccess());

        CommentRequestDto dto = new CommentRequestDto();
        dto.setContent("내용");

        taskApiClient.createComment(1L, 1L, dto);
        server.verify();
    }

    @Test
    void updateComment() {
        String json = """
                {
                    "content": "내용"
                }
                """;

        server.expect(requestTo(PREFIX + "/task-api/tasks/1/comments/1"))
                .andExpect(method(PUT))
                .andExpect(header("X-Account-Id", "1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json))
                .andRespond(withSuccess());

        CommentRequestDto dto = new CommentRequestDto();
        dto.setContent("내용");

        taskApiClient.updateComment(1L, 1L, 1L, dto);
        server.verify();
    }

    @Test
    void deleteComment() {
        server.expect(requestTo(PREFIX + "/task-api/tasks/1/comments/1"))
                .andExpect(method(DELETE))
                .andExpect(header("X-Account-Id", "1"))
                .andRespond(withSuccess());

        taskApiClient.deleteComment(1L, 1L, 1L);
        server.verify();
    }

    @Test
    void getMyComments() {
        String json = """
                {
                    "commentResponseList": [
                        {
                            "id": 1,
                            "accountId": 1,
                            "userId": "유저아이디",
                            "content": "내용",
                            "createdAt": "2026-05-22T00:00:00",
                            "updatedAt": "2026-05-22T00:00:00"
                        }
                    ]
                }
                """;
        server.expect(requestTo(PREFIX + "/task-api/mypage/comments"))
                .andExpect(header("X-Account-Id", "1"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        CommentListDto result = taskApiClient.getMyComments(1L);
        assertThat(result).isNotNull();
        assertThat(result.getCommentResponseDtoList().getFirst().getId()).isEqualTo(1);
        assertThat(result.getCommentResponseDtoList().getFirst().getAccountId()).isEqualTo(1);
        assertThat(result.getCommentResponseDtoList().getFirst().getUserId()).isEqualTo("유저아이디");
        assertThat(result.getCommentResponseDtoList().getFirst().getContent()).isEqualTo("내용");
    }

    // ---------------------- Milestone ----------------------

    @Test
    void createMilestone() {
        String json = """
                {
                    "title": "제목",
                    "description": "내용",
                    "status": "PLANNED",
                    "dueDate": "2026-05-22T00:00:00"
                }
                """;

        server.expect(requestTo(PREFIX + "/task-api/tasks/1/milestones"))
                .andExpect(method(POST))
                .andExpect(header("X-Account-Id", "1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json))
                .andRespond(withSuccess());

        MilestoneRequestDto dto = new MilestoneRequestDto();
        dto.setTitle("제목");
        dto.setDescription("내용");
        dto.setStatus(MilestoneStatus.PLANNED);
        dto.setDueDate(LocalDateTime.of(2026, 5, 22, 0, 0, 0));

        taskApiClient.createMilestone(1L, 1L, dto);
        server.verify();
    }

    @Test
    void updateMilestone() {
        String json = """
                {
                    "title": "제목",
                    "description": "내용",
                    "status": "PLANNED",
                    "dueDate": "2026-05-22T00:00:00"
                }
                """;

        server.expect(requestTo(PREFIX + "/task-api/tasks/1/milestones"))
                .andExpect(method(PUT))
                .andExpect(header("X-Account-Id", "1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json))
                .andRespond(withSuccess());

        MilestoneRequestDto dto = new MilestoneRequestDto();
        dto.setTitle("제목");
        dto.setDescription("내용");
        dto.setStatus(MilestoneStatus.PLANNED);
        dto.setDueDate(LocalDateTime.of(2026, 5, 22, 0, 0, 0));

        taskApiClient.updateMilestone(1L, 1L, dto);
        server.verify();
    }

    @Test
    void deleteMilestone() {
        server.expect(requestTo(PREFIX + "/task-api/tasks/1/milestones"))
                .andExpect(method(DELETE))
                .andExpect(header("X-Account-Id", "1"))
                .andRespond(withSuccess());

        taskApiClient.deleteMilestone(1L, 1L);
        server.verify();
    }
}