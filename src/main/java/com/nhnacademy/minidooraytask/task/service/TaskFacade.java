package com.nhnacademy.minidooraytask.task.service;

import com.nhnacademy.minidooraytask.MileStone.domain.MileStone;
import com.nhnacademy.minidooraytask.MileStone.domain.MileStoneStatus;
import com.nhnacademy.minidooraytask.MileStone.domain.MilestoneResponseDto;
import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import com.nhnacademy.minidooraytask.member.service.ProjectMemberService;
import com.nhnacademy.minidooraytask.project.domain.Project;
import com.nhnacademy.minidooraytask.project.domain.ProjectInfoDto;
import com.nhnacademy.minidooraytask.project.service.ProjectService;
import com.nhnacademy.minidooraytask.tag.domain.Tag;
import com.nhnacademy.minidooraytask.tag.domain.TagResponseDto;
import com.nhnacademy.minidooraytask.tag.domain.TaskTag;
import com.nhnacademy.minidooraytask.task.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TaskFacade {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final ProjectMemberService projectMemberService;

    @Transactional
    public TaskInfoListDto getTaskInfoList(long projectId, long accountId) {
        projectMemberService.checkProjectMember(projectId, accountId);

        return createTaskInfoListDto(projectMemberService.getProjectMemberByAccountId(accountId)).get(projectId);
    }

    @Transactional
    public TaskViewDto getSpecificTask(Long taskId, Long projectId, long accountId) {
        projectMemberService.checkIncludedMember(projectId, accountId);

        Task task = taskService.getTaskById(taskId);

        MileStone mileStone = task.getMilestone();
        MilestoneResponseDto milestoneResponseDto = new MilestoneResponseDto(mileStone.getId(),
                mileStone.getTitle(), mileStone.getDescription(), mileStone.getStatus(), mileStone.getDueDate(),
                mileStone.getCreatedAt(), mileStone.getUpdatedAt());

        List<TagResponseDto> tagResponseDtoList = task.getTaskTagList().stream()
                .map(TaskTag::getTag)
                .map(t ->
                        new TagResponseDto(t.getId(), t.getName()))
                .toList();

        TaskResponseDto taskResponseDto = new TaskResponseDto(task.getId(), task.getProject().getId(),
                task.getProjectMember().getId(), task.getTitle(), task.getContent(), task.getCreatedAt(),
                task.getUpdatedAt(), milestoneResponseDto, tagResponseDtoList);

        ProjectMember projectMember = projectMemberService.getProjectMemberByProjectIdAndAccountId(projectId, accountId);
        TaskInfoListDto taskInfoListDto = createTaskInfoListDto(List.of(projectMember)).get(projectId);

        Project project = task.getProject();
        List<MileStoneStatus> mileStoneStatusList = project.getTaskList().stream()
                .map(t -> t.getMilestone().getStatus())
                .toList();

        ProjectInfoDto projectInfoDto = new ProjectInfoDto(project.getId(), project.getTitle(),
                project.getStatus(), mileStoneStatusList);

        return new TaskViewDto(taskResponseDto, taskInfoListDto, projectInfoDto);
    }

    public Map<Long, TaskInfoListDto> createTaskInfoListDto(List<ProjectMember> projectMemberList) {
        List<Task> tasks = projectMemberList.stream()
                .map(ProjectMember::getTaskList)
                .flatMap(Collection::stream)
                .toList();

        Map<Long, MileStoneStatus> mileStoneStatusMapeMap = tasks.stream()
                .map(Task::getMilestone)
                .collect(Collectors.toMap(ms -> ms.getTask().getId(), MileStone::getStatus));

        Set<Long> projectIds = tasks.stream()
                .map(t -> t.getProject().getId())
                .collect(Collectors.toSet());

        Map<Long, TaskInfoListDto> taskListMap = new HashMap<>();
        for(Long id : projectIds) {
            List<TaskInfoDto> taskInfoList = tasks.stream()
                    .filter(t -> t.getProject().getId().equals(id))
                    .map(t -> new TaskInfoDto(t.getId(), t.getTitle(), mileStoneStatusMapeMap.get(t.getId())))
                    .toList();

            taskListMap.put(id, new TaskInfoListDto(taskInfoList));
        }

        return taskListMap;
    }

    @Transactional
    public void createTask(Long projectId, Long accountId, TaskRequestDto taskRequestDto) {

        //projecId, accountId로 member가 존제하는지
        Project project = projectService.exGetProjectById(projectId);

        //요청자가 삭제되지 않은 정상 멤버인지 확인하고 그 멤버 객체 가져
        ProjectMember activeMember = projectMemberService.getActiveMember(projectId, accountId);

        taskService.createTask(project, activeMember, taskRequestDto);

    }

    @Transactional
    public void updateTask(Long projectId, Long taskId, Long accountId, TaskRequestDto taskrequestDto) {

        //권한이 있는 멤버인지(삭제된 멤버가 아닌지)
        ProjectMember activeMember = projectMemberService.getActiveMember(projectId, accountId);

        //작성자 확인
        taskService.checkTaskMaker(activeMember.getId(), taskId);

        //태그가있는지없는지 없으면 만들기(태크서비스) 업데이트하는 테스크 객체랑 taskrequestDto 태그리스트 같이 넘기기


    }

}
