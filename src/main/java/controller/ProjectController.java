package controller;


import ch.qos.logback.core.model.processor.PhaseIndicator;
import com.nhnacademy.minidooraytask.project.domain.*;
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
    public ResponseEntity<ProjectViewDto> getMyProjects(@RequestHeader("X-Account-Id") long accountId) {
        ProjectViewDto responseDto = null;
        return ResponseEntity.ok().body(responseDto);
    }


    // POST - 프로젝트 생성
    @PostMapping
    public ResponseEntity<Void> createProject(@RequestHeader("X-Account-Id") long accountId,
                                              @RequestBody ProjectRequestDto requestDto) {
//        projectService.createProject(accountId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    // PATCH - 프로젝트 상태 변경
    @PatchMapping("/{projectId}/status")
    public ResponseEntity<Void> updateProjectStatus(@RequestHeader("X-Account-Id") long projectId,
                                                    @RequestBody ProjectUpdateRequestDto requestDto) {
//        projectService.updateProjectStatus(projectId, requestDto);
        return ResponseEntity.ok().build();
    }
}
