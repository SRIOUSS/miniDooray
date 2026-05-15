package controller;


import com.nhnacademy.minidooraytask.project.domain.Project;
import com.nhnacademy.minidooraytask.project.domain.ProjectCreateRequestDto;
import com.nhnacademy.minidooraytask.project.domain.ProjectResponseDto;
import com.nhnacademy.minidooraytask.project.domain.ProjectStatus;
import com.nhnacademy.minidooraytask.project.respository.ProjectRepository;
import com.nhnacademy.minidooraytask.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    //내 프로젝트 목록 조회
    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getMyProjects(
            @RequestHeader("X-Account-Id") Long accountId) {
        return ResponseEntity.ok(projectService.getMyProjects(accountId));
    }

    // GET - 특정 프로젝트 상세 조회
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> getProjectById(
            @PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    // POST - 프로젝트 생성
    @PostMapping
    public ResponseEntity<Void> createProject(
            @RequestHeader("X-Account-Id") Long accountId,
            @RequestBody ProjectCreateRequestDto request) {
        projectService.createProject(accountId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // PATCH - 프로젝트 상태 변경
    @PatchMapping("/{projectId}/status")
    public ResponseEntity<Void> updateProjectStatus(
            @PathVariable Long projectId,
            @RequestParam ProjectStatus status) {
        projectService.updateProjectStatus(projectId, status);
        return ResponseEntity.ok().build();
    }
}
