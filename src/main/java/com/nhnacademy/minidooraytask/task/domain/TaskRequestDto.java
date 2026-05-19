package com.nhnacademy.minidooraytask.task.domain;

import java.util.List;

public record TaskRequestDto(
        String title,
        String content,
        List<String> tagNameList
) {
}
