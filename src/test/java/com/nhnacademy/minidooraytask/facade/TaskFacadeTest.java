package com.nhnacademy.minidooraytask.facade;

import com.nhnacademy.minidooraytask.MileStone.domain.MileStone;
import com.nhnacademy.minidooraytask.MileStone.domain.MileStoneStatus;
import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import com.nhnacademy.minidooraytask.member.service.ProjectMemberService;
import com.nhnacademy.minidooraytask.project.domain.Project;
import com.nhnacademy.minidooraytask.project.service.ProjectService;
import com.nhnacademy.minidooraytask.tag.service.TagService;
import com.nhnacademy.minidooraytask.task.domain.*;
import com.nhnacademy.minidooraytask.task.service.TaskFacade;
import com.nhnacademy.minidooraytask.task.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TaskFacade.class)
public class TaskFacadeTest {

    @Autowired
    private TaskFacade taskFacade;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private ProjectMemberService projectMemberService;

    @MockitoBean
    private TagService tagService;

    @Test
    @DisplayName("Facade: Task 생성 흐름 검증 - 성공")
    void createdTask_success() {
        long projectId = 1L;
        long accountId = 2L;

        TaskRequestDto taskRequestDto = new TaskRequestDto("title", "content", List.of("tag1", "tag2"));

        Project mockProject = mock(Project.class);
        ProjectMember mockPmember = mock(ProjectMember.class);
        Task mockTask = mock(Task.class);

        given(projectService.exGetProjectById(projectId)).willReturn(mockProject);
        given(projectMemberService.getActiveMember(projectId, accountId)).willReturn(mockPmember);
        given(taskService.createTask(mockProject, mockPmember, taskRequestDto)).willReturn(mockTask);
        willDoNothing().given(tagService).connectTag(mockTask, taskRequestDto.tagNameList());

        taskFacade.createTask(projectId, accountId, taskRequestDto);

        then(projectService).should().exGetProjectById(projectId);
        then(projectMemberService).should().getActiveMember(projectId, accountId);
        then(taskService).should().createTask(mockProject, mockPmember, taskRequestDto);
        then(tagService).should().connectTag(mockTask, taskRequestDto.tagNameList());
    }

    @Test
    @DisplayName("Facade: Task 수정 - 성공")
    void updateTask_success() {
        long projectId = 1L;
        long taskId = 10L;
        long accountId = 100L;
        long memberId = 50L;
        TaskRequestDto requestDto = new TaskRequestDto("modititle", "modicontent", List.of("newTag"));

        ProjectMember mockMember = mock(ProjectMember.class);
        Task verifiedTask = mock(Task.class);
        Task updatedTask = mock(Task.class);
        TaskResponseDto mockResponseDto = mock(TaskResponseDto.class);

        given(projectMemberService.getActiveMember(projectId, accountId)).willReturn(mockMember);
        given(mockMember.getId()).willReturn(memberId);

        willDoNothing().given(taskService).checkTaskMaker(memberId, taskId);

        given(taskService.getTaskById(taskId)).willReturn(verifiedTask);
        given(taskService.updateTask(verifiedTask, requestDto)).willReturn(updatedTask);
        willDoNothing().given(tagService).connectTag(updatedTask, requestDto.tagNameList());
        given(taskService.buildTaskResponseDto(updatedTask)).willReturn(mockResponseDto);

        TaskResponseDto result = taskFacade.updateTask(projectId, taskId, accountId, requestDto);

        assertThat(result).isEqualTo(mockResponseDto);
        then(projectMemberService).should().getActiveMember(projectId, accountId);
        then(taskService).should().checkTaskMaker(memberId, taskId);
        then(taskService).should().updateTask(verifiedTask, requestDto);
        then(tagService).should().connectTag(updatedTask, requestDto.tagNameList());
    }

    @Test
    @DisplayName("Facade: Task 삭제 - 성공")
    void deleteTask_success() {
        long projectId = 1L;
        long accountId = 2L;
        long taskId = 3L;
        long memberId = 10L;

        ProjectMember mockMember = mock(ProjectMember.class);
        Task mockTask = mock(Task.class);

        given(projectMemberService.getActiveMember(projectId, accountId)).willReturn(mockMember);
        given(mockMember.getId()).willReturn(memberId);
        willDoNothing().given(taskService).checkTaskMaker(memberId, taskId);
        given(taskService.getTaskById(taskId)).willReturn(mockTask);
        willDoNothing().given(taskService).deleteTask(mockTask);

        taskFacade.deleteTask(projectId, accountId, taskId);

        then(projectMemberService).should().getActiveMember(projectId, accountId);
        then(taskService).should().checkTaskMaker(memberId, taskId);
        then(taskService).should().deleteTask(mockTask);
    }

    @Test
    @DisplayName("Facade: 내 Task 조회 - 마일스톤이 없는 경우 (삼항 연산자 분기 커버)")
    void getMyTasks_nullMilestone() {
        long accountId = 1L;
        Task mockTask = mock(Task.class);

        given(mockTask.getId()).willReturn(10L);
        given(mockTask.getTitle()).willReturn("마일스톤 없는 작업");
        given(mockTask.getMilestone()).willReturn(null);

        given(taskService.getMytasks(accountId)).willReturn(List.of(mockTask));

        TaskInfoListDto result = taskFacade.getMyTasks(accountId);

        assertThat(result).isNotNull();
        assertThat(result.taskInfoDtoList()).hasSize(1);

        // TaskInfoDto 레코드의 필드명에 따라 status() 또는 mileStoneStatus() 자동완성을 사용해주세요.
        assertThat(result.taskInfoDtoList().get(0).status()).isNull();
    }

    @Test
    @DisplayName("Facade: 프로젝트별 TaskInfo 맵 생성 - 삭제된 태스크 필터링 및 마일스톤 유무 커버")
    void createTaskInfoListDto_success() {
        ProjectMember mockMember = mock(ProjectMember.class);
        Project mockProject = mock(Project.class);
        Task taskWithMilestone = mock(Task.class);
        Task taskWithoutMilestone = mock(Task.class);
        Task taskDeleted = mock(Task.class);

        MileStone mockMilestone = mock(MileStone.class);

        given(mockMember.getProject()).willReturn(mockProject);
        given(mockProject.getTaskList()).willReturn(List.of(taskWithMilestone, taskWithoutMilestone, taskDeleted));
        given(mockProject.getId()).willReturn(100L);

        // 1. 마일스톤이 있는 정상 태스크
        given(taskWithMilestone.isDeleted()).willReturn(false);
        given(taskWithMilestone.getId()).willReturn(1L);
        given(taskWithMilestone.getProject()).willReturn(mockProject);
        given(taskWithMilestone.getMilestone()).willReturn(mockMilestone);
        given(mockMilestone.getStatus()).willReturn(MileStoneStatus.IN_PROGRESS);

        // 2. 마일스톤이 없는 정상 태스크
        given(taskWithoutMilestone.isDeleted()).willReturn(false);
        given(taskWithoutMilestone.getId()).willReturn(2L);
        given(taskWithoutMilestone.getProject()).willReturn(mockProject);
        given(taskWithoutMilestone.getMilestone()).willReturn(null);

        // 3. 삭제된 태스크 (필터링 되어야 함)
        given(taskDeleted.isDeleted()).willReturn(true);

        Map<Long, TaskInfoListDto> result = taskFacade.createTaskInfoListDto(List.of(mockMember));

        assertThat(result).containsKey(100L);
        assertThat(result.get(100L).taskInfoDtoList()).hasSize(2);
    }

    @Test
    @DisplayName("Facade: 특정 Task 상세 조회 - 마일스톤/태그/코멘트가 전부 비어있는 경우 스킵 검증")
    void getSpecificTask_emptyAssociations() {
        long taskId = 1L;
        long projectId = 2L;
        long accountId = 3L;

        Task mockTask = mock(Task.class);
        Project mockProject = mock(Project.class);
        ProjectMember mockMember = mock(ProjectMember.class);

        willDoNothing().given(projectMemberService).checkIncludedMember(projectId, accountId);
        given(taskService.getTaskById(taskId)).willReturn(mockTask);

        given(mockTask.getMilestone()).willReturn(null);
        given(mockTask.getTaskTagList()).willReturn(Collections.emptyList());

        given(mockTask.getId()).willReturn(taskId);
        given(mockTask.getTitle()).willReturn("상세조회 제목");
        given(mockTask.getProject()).willReturn(mockProject);
        given(mockTask.getProjectMember()).willReturn(mockMember);

        given(projectMemberService.getProjectMemberByProjectIdAndAccountId(projectId, accountId)).willReturn(mockMember);
        given(mockMember.getProject()).willReturn(mockProject);
        given(mockProject.getId()).willReturn(projectId);
        given(mockProject.getTaskList()).willReturn(List.of(mockTask));
        given(mockTask.isDeleted()).willReturn(false);

        given(mockTask.getCommentList()).willReturn(Collections.emptyList());

        TaskViewDto result = taskFacade.getSpecificTask(taskId, projectId, accountId);

        assertThat(result).isNotNull();

        // Record 방식 필드명으로 매칭 및 isEmpty() 사용
        assertThat(result.taskResponseDto().milestoneResponseDto()).isNull();
        assertThat(result.taskResponseDto().tagResponseDtoList()).isEmpty();
        assertThat(result.commentResponseDtoList()).isEmpty();

        then(projectMemberService).should(never()).getMemberMapWithUserIdByMemberId(any());
    }
}