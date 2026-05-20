package com.nhnacademy.minidooraytask.project.domain;

import com.nhnacademy.minidooraytask.task.domain.TaskInfoDto;

import java.util.List;

public record ProjectViewDto(
        List<ProjectInfoDto> projectInfoDtoList,
        List<TaskInfoDto> taskInfoDtoList
) {}
