package com.nhnacademy.minidooraytask.project.service;

import com.nhnacademy.minidooraytask.member.domain.MembersAuth;
import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import com.nhnacademy.minidooraytask.member.repository.ProjectMemberRepository;
import com.nhnacademy.minidooraytask.project.domain.Project;
import com.nhnacademy.minidooraytask.project.domain.ProjectCreateRequestDto;
import com.nhnacademy.minidooraytask.project.domain.ProjectResponseDto;
import com.nhnacademy.minidooraytask.project.domain.ProjectUpdateRequestDto;
import com.nhnacademy.minidooraytask.project.exception.ProjectNotFoundException;
import com.nhnacademy.minidooraytask.project.respository.ProjectRepository;
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
    private final ProjectMemberRepository projectMemberRepository; // 1. 멤버 저장을 위해 리포지토리 주입 추가

    // 내 프로젝트 목록 조회
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getMyProjects(Long accountId) {
        return projectRepository.findAllByAccountIdByQuery(accountId)
                .stream()
                .map(ProjectResponseDto::from)
                .toList();
    }

    // GET - 특정 프로젝트 상세 조회
    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다. projectId: " + projectId));
        return ProjectResponseDto.from(project);
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
                    log.debug("[project service] 존재하지 않는 프로젝트입니다 - projectId : {}", projectId);
                    return new ProjectNotFoundException("[project service] 존재하지 않는 프로젝트입니다");
                });

        project.updateProjectInfo(requestDto.title(), requestDto.description(), requestDto.status());
    }
}
