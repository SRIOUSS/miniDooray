package com.nhnacademy.minidooraytask.task.domain;

import com.nhnacademy.minidooraytask.milestone.domain.MileStoneStatus;

public record TaskInfoDto(
        long id,
        String title,
        MileStoneStatus status
) {
}
