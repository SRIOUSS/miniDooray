package com.nhnacademy.minidooraytask.project.service;


import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import com.nhnacademy.minidooraytask.member.service.ProjectMemberService;
import com.nhnacademy.minidooraytask.project.domain.ProjectInfoDto;
import com.nhnacademy.minidooraytask.project.domain.ProjectViewDto;
import com.nhnacademy.minidooraytask.task.domain.TaskInfoDto;
import com.nhnacademy.minidooraytask.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ProjectFacade {

    private final ProjectService projectService;
    private final TaskService taskService;
    private final ProjectMemberService projectMemberService;

    @Transactional
    public ProjectViewDto getProjectView(Long accountId) {


        //사용자가 참여중인 프로젝트 멤버 리스트 가져오기
        List<ProjectMember> projectMemberList = projectMemberService.getProjectMemberByAccountId(accountId);

//        projectInfoDto랑 TaskInfoDto를 가져와서 ProjetViewDto에 넣어야함
        List<ProjectInfoDto> projectInfoDtos = projectMemberList.stream()
                .map(ProjectMember::getProject)
                .map(project -> new ProjectInfoDto(
                  project.getId(),
                    project.getTitle(),
                    project.getStatus(),
                    project.getTaskList().stream().map(t -> t.getMilestone().getStatus()).toList()

                ))
                .toList();

        // task -> taskinfoDto
        List<TaskInfoDto> taskInfoDtos = projectMemberList.stream()
                .map(ProjectMember::getTaskList)
                .flatMap(Collection::stream)
                .map(task -> new TaskInfoDto(
                        task.getId(),
                        task.getTitle(),
                        task.getMilestone().getStatus()
                ))
                .toList();


        return new ProjectViewDto(projectInfoDtos,taskInfoDtos);
    }

}
