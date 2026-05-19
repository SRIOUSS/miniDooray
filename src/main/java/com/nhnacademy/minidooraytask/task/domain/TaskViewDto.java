package com.nhnacademy.minidooraytask.task.domain;

import com.nhnacademy.minidooraytask.project.domain.ProjectInfoDto;

public record TaskViewDto(
        TaskResponseDto taskResponseDto,
        TaskInfoListDto taskInfoListDto,
        ProjectInfoDto projectInfoDto
) {
}
