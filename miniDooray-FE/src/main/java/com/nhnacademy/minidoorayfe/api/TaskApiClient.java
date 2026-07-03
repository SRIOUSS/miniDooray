package com.nhnacademy.minidoorayfe.api;

import com.nhnacademy.minidoorayfe.dto.comment.CommentListDto;
import com.nhnacademy.minidoorayfe.dto.comment.CommentRequestDto;
import com.nhnacademy.minidoorayfe.dto.member.MemberInfoListDto;
import com.nhnacademy.minidoorayfe.dto.member.MemberRequestDto;
import com.nhnacademy.minidoorayfe.dto.milestone.MilestoneRequestDto;
import com.nhnacademy.minidoorayfe.dto.project.ProjectRequestDto;
import com.nhnacademy.minidoorayfe.dto.project.ProjectViewDto;
import com.nhnacademy.minidoorayfe.dto.task.TaskInfoListDto;
import com.nhnacademy.minidoorayfe.dto.task.TaskRequestDto;
import com.nhnacademy.minidoorayfe.dto.task.TaskViewDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TaskApiClient {

    private static final String ACCOUNT_ID_HEADER = "X-Account-Id";

    private final RestClient restClient;

    public TaskApiClient(@Qualifier("gatewayRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    // --------------------------------------- Project ----------------------------------------------

    // 로그인 후 프로젝트 페이지 요청 (GET)
    public ProjectViewDto getProjects(Long accountId) {

        return this.restClient.get()
                .uri("/task-api/projects")
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .retrieve()
                .body(ProjectViewDto.class);
    }

    // 프로젝트 생성 요청 (POST)
    public void createProject(ProjectRequestDto dto, Long accountId) {

        this.restClient.post()
                .uri("/task-api/projects")
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    // 프로젝트 수정 요청 (PUT)
    public void updateProject(Long projectId, ProjectRequestDto dto, Long accountId) {

        this.restClient.put()
                .uri("/task-api/projects/{projectId}", projectId)
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    // 특정 프로젝트 삭제 (DELETE) (소프트삭제)
    public void deleteProject(Long projectId, Long accountId) {

        this.restClient.delete()
                .uri("/task-api/projects/{projectId}", projectId)
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .retrieve()
                .toBodilessEntity();
    }


    // --------------------------------------- Member ----------------------------------------------

    // 특정 프로젝트의 전체 멤버 목록 조회 (GET)
    public MemberInfoListDto getMembers(Long projectId, Long accountId) {

        return this.restClient.get()
                .uri("/task-api/projects/{projectId}/members", projectId)
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .retrieve()
                .body(MemberInfoListDto.class);
    }

    // 프로젝트의 멤버 추가 요청 (POST)
    public void addMember(Long projectId, Long accountId, MemberRequestDto dto) {

        this.restClient.post()
                .uri("/task-api/projects/{projectId}/members", projectId)
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    // 프로젝트의 멤버 권한 변경
    public void updateMemberAuth(Long projectId, Long memberId, MemberRequestDto dto, Long accountId) {

        this.restClient.put()
                .uri("/task-api/projects/{projectId}/members/{memberId}", projectId, memberId)
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    // 프로젝트의 멤버 삭제 (DELETE)
    public void deleteMember(Long projectId, Long accountId, Long memberId) {

        this.restClient.delete()
                .uri("/task-api/projects/{projectId}/members/{memberId}", projectId, memberId)
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .retrieve()
                .toBodilessEntity();
    }

    // --------------------------------------- Task ----------------------------------------------

    // 특정 Project의 Task 목록 요청 (GET)
    public TaskInfoListDto getTasks(Long projectId, Long accountId) {

        return this.restClient.get()
                .uri("/task-api/projects/{projectId}/tasks", projectId)
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .retrieve().body(TaskInfoListDto.class);
    }

    // 특정 Task 정보 요청 (GET)
    public TaskViewDto getTask(Long projectId, Long taskId, Long accountId) {

        return this.restClient.get()
                .uri("/task-api/projects/{projectId}/tasks/{taskId}", projectId, taskId)
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .retrieve()
                .body(TaskViewDto.class);
    }

    // Task 생성 (POST)
    public void createTask(Long projectId, Long accountId, TaskRequestDto dto) {

        this.restClient.post()
                .uri("/task-api/projects/{projectId}/tasks", projectId)
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    // 특정 Task 수정 (PUT)
    public void updateTask(Long projectId, Long taskId, Long accountId, TaskRequestDto dto) {

        this.restClient.put()
                .uri("/task-api/projects/{projectId}/tasks/{taskId}", projectId, taskId)
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    // 특정 Task 삭제 (DELETE) (소프트삭제)
    public void deleteTask(Long projectId, Long taskId, Long accountId) {

        this.restClient.delete()
                .uri("/task-api/projects/{projectId}/tasks/{taskId}", projectId, taskId)
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .retrieve()
                .toBodilessEntity();
    }

    // 마이페이지에서 내가 작성한 Task 목록 (GET)
    public TaskInfoListDto getMyTasks(Long accountId) {

        return this.restClient.get()
                .uri("/task-api/mypage/tasks")
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .retrieve()
                .body(TaskInfoListDto.class);
    }

    // --------------------------------------- Comment ----------------------------------------------

    // 댓글 생성 (POST)
    public void createComment(Long taskId, Long accountId, CommentRequestDto dto) {
        String url = "/task-api/tasks/%s/comments".formatted(taskId);
        restClient.post()
                .uri(url)
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    // 댓글 수정 (PUT)
    public void updateComment(Long taskId, Long accountId, Long commentId, CommentRequestDto dto) {
        String url = "/task-api/tasks/%s/comments/%s".formatted(taskId, commentId);
        restClient.put()
                .uri(url)
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    // 댓글 삭제 (DELETE)
    public void deleteComment(Long taskId, Long accountId, Long commentId) {
        String url = "/task-api/tasks/%s/comments/%s".formatted(taskId, commentId);
        restClient.delete().uri(url).header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .retrieve().toBodilessEntity();
    }

    // 마이페이지에서 내가 작성한 Comment 목록 (GET)
    public CommentListDto getMyComments(Long accountId) {
        String url = "/task-api/mypage/comments";
        return restClient.get()
                .uri(url)
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .retrieve()
                .body(CommentListDto.class);
    }

    // --------------------------------------- Milestone ----------------------------------------------

    // 마일스톤 생성 (POST)
    public void createMilestone(Long taskId, Long accountId, MilestoneRequestDto dto) {
        String url = "/task-api/tasks/%s/milestones".formatted(taskId);
        restClient.post()
                .uri(url)
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    // 마일스톤 수정 (PUT)
    public void updateMilestone(Long taskId, Long accountId, MilestoneRequestDto dto) {
        String url = "/task-api/tasks/%s/milestones".formatted(taskId);
        restClient.put()
                .uri(url)
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    // 마일스톤 삭제 (DELETE)
    public void deleteMilestone(Long taskId, Long accountId) {
        String url = "/task-api/tasks/%s/milestones".formatted(taskId);
        restClient.delete()
                .uri(url)
                .header(ACCOUNT_ID_HEADER, String.valueOf(accountId))
                .retrieve()
                .toBodilessEntity();
    }
}