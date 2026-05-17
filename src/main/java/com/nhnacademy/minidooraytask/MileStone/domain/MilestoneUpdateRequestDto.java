package com.nhnacademy.minidooraytask.MileStone.domain;

import java.time.LocalDateTime;

public record MilestoneUpdateRequestDto(
        String title,
        String description,
        MileStoneStatus status,
        LocalDateTime dueDate
) {
}
