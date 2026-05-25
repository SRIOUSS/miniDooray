package com.nhnacademy.minidooraytask.milestone.domain;

import java.time.LocalDateTime;

public record MilestoneRequestDto(
        String title,
        String description,
        MileStoneStatus status,
        LocalDateTime dueDate
) {
}
