package com.nhnacademy.minidooraytask.milestone.domain;

import java.time.LocalDateTime;

public record MilestoneUpdateRequestDto(
        String title,
        String description,
        MileStoneStatus status,
        LocalDateTime dueDate
) {
}
