package com.nhnacademy.minidooraytask.MileStone.domain;

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
