package com.nhnacademy.minidooraytask.project.service;

import com.nhnacademy.minidooraytask.member.domain.MembersAuth;
import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import com.nhnacademy.minidooraytask.member.repository.ProjectMemberRepository;
import com.nhnacademy.minidooraytask.project.domain.Project;
import com.nhnacademy.minidooraytask.project.domain.ProjectCreateRequestDto;
import com.nhnacademy.minidooraytask.project.domain.ProjectResponseDto;
import com.nhnacademy.minidooraytask.project.domain.ProjectUpdateRequestDto;
import com.nhnacademy.minidooraytask.project.exception.NoAuthoProjectException;
import com.nhnacademy.minidooraytask.project.exception.ProjectNotFoundException;
import com.nhnacademy.minidooraytask.project.respository.ProjectRepository;
import com.nhnacademy.minidooraytask.task.domain.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    //프로젝트 단건 조회
    @Transactional(readOnly = true)
    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    log.debug("[project service] 존재하지 않는 proeject입니다 - projectId:{}", projectId);
                    return new ProjectNotFoundException("[project service] 존재하지 않는 proeject입니다");
                });
    }

    // 내 프로젝트 목록 조회
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getMyProjects(Long accountId) {
        return projectRepository.findAllByAccountIdByQuery(accountId)
                .stream()
                .map(ProjectResponseDto::from)
                .toList();
    }

    //프로젝트 관리자인지 확인(project 삭제시 필요)
    @Transactional(readOnly = true)
    public void checkProjectAdmin(Long projectId, Long accountId) {
        if( !(projectRepository.existByIdAndCreateAccountId(projectId, accountId))) {
            log.debug("[project service] 해당 accountId는 project에 권한이 존재하지 않습니다 - projectId : {}, accountId : {}", projectId, accountId);
            throw new NoAuthoProjectException("[project service] 해당 accountId는 project에 권한이 존재하지 않습니다");
        }
    }


    // GET - 특정 프로젝트 상세 조회
    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다. projectId: " + projectId));
        return ProjectResponseDto.from(project);
    }

    @Transactional
    public Project exGetProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("존재하지 않는 프로젝트입니다. projectId: " + projectId));
    }

    // POST 프로젝트 생성, 관리자 등록
    @Transactional
    public ProjectResponseDto createProject(Long accountId, ProjectCreateRequestDto request) {

        Project project = new Project(
                request.getTitle(),
                request.getDescription(),
                accountId
        );

        Project savedProject = projectRepository.save(project);

        //생성된 프로젝트의 accountId 로 관리자 생성
        ProjectMember adminMember = new ProjectMember(
                savedProject,
                accountId,
                MembersAuth.ADMIN
        );

        projectMemberRepository.save(adminMember);

        // dto
        return ProjectResponseDto.from(savedProject);
    }

    // PUT - 프로젝트 수정
    @Transactional
    public void updateProject(Long projectId, ProjectUpdateRequestDto requestDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    log.debug("[project service] 존재하지 않는 프로젝트 업데이트입니다 - projectId : {}", projectId);
                    return new ProjectNotFoundException("[project service] 존재하지 않는 프로젝트입니다");
                });

        project.updateProjectInfo(requestDto.title(), requestDto.description(), requestDto.status());
        projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long projectId, Long accountId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    log.debug("[project service] 존재하지 않는 프로젝트 삭제입니다 - projectId : {}", projectId);
                    return new ProjectNotFoundException("[project service] 존재하지 않는 프로젝트입니다");
                });

        project.isDelete();
        projectRepository.save(project);
    }
}
