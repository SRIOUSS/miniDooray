package com.nhnacademy.minidooraytask.project.service;

import com.nhnacademy.minidooraytask.project.domain.Project;
import com.nhnacademy.minidooraytask.project.domain.ProjectCreateRequestDto;
import com.nhnacademy.minidooraytask.project.domain.ProjectResponseDto;
import com.nhnacademy.minidooraytask.project.domain.ProjectStatus;
import com.nhnacademy.minidooraytask.project.respository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    // GET - 내 프로젝트 목록 조회
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
        Project project = projectRepository.findByProjectId(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다. projectId: " + projectId));
        return ProjectResponseDto.from(project);
    }

    // POST - 프로젝트 생성
    @Transactional
    public void createProject(Long accountId, ProjectCreateRequestDto request) {
        Project project = new Project(
                request.getTitle(),
                request.getDescription(),
                accountId
        );
        projectRepository.save(project);
    }

    // PATCH - 프로젝트 상태 변경
    @Transactional
    public void updateProjectStatus(Long projectId, ProjectStatus status) {
        projectRepository.findByProjectId(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다. projectId: " + projectId));

        projectRepository.updateProjectStatus(projectId, status);
    }
}
