package com.nhnacademy.minidooraytask.milestone.domain;

import java.time.LocalDateTime;

public record MilestoneResponseDto(
        long id,
        String title,
        String description,
        MileStoneStatus status,
        LocalDateTime dueDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
