package com.nhnacademy.minidooraytask.task.domain;

import java.util.List;

public record TaskCreateRequestDto(
        String title,
        String content,
        Long milestoneId,
        List<Long> tagIds
) {
}
