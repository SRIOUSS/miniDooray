package com.nhnacademy.minidooraytask.project.domain;

import com.nhnacademy.minidooraytask.project.respository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProjectResponseDto {

    private Long projectId;
    private String title;
    private String description;
    private ProjectStatus status;
    private LocalDateTime createdAt;
    private Long createdAccountId;

    // entity > DTO 전환
    public static ProjectResponseDto from(Project project) {

        return new ProjectResponseDto(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getStatus(),
                project.getCreatedAt(),
                project.getCreateAccountId()
        );
    }
}
