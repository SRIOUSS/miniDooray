package com.nhnacademy.minidooraytask.task.domain;

import com.nhnacademy.minidooraytask.milestone.domain.MilestoneResponseDto;
import com.nhnacademy.minidooraytask.tag.domain.TagResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public record TaskResponseDto(
        long taskId,
        long projectId,
        Long createMemberId,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        MilestoneResponseDto milestoneResponseDto,
        List<TagResponseDto> tagResponseDtoList
) {
}
