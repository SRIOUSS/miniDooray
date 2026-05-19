package com.nhnacademy.minidooraytask.task.domain;

import com.nhnacademy.minidooraytask.MileStone.domain.MileStoneStatus;

public record TaskInfoDto(
        long id,
        String title,
        MileStoneStatus status
) {
}
