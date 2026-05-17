package com.nhnacademy.minidooraytask.MileStone.domain;

import java.time.LocalDateTime;

public record MilestoneCreateRequestDto(
        String title,
        String description,
        MileStoneStatus status,
        LocalDateTime dueDate
) {
}
