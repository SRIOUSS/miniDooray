package com.nhnacademy.minidooraytask.task.domain;

import com.nhnacademy.minidooraytask.comment.domain.CommentResponseDto;
import com.nhnacademy.minidooraytask.project.domain.ProjectInfoDto;

import java.util.List;

public record TaskViewDto(
        TaskResponseDto taskResponseDto,
        TaskInfoListDto taskInfoListDto,
        ProjectInfoDto projectInfoDto,
        List<CommentResponseDto> commentResponseDtoList
) {
}
