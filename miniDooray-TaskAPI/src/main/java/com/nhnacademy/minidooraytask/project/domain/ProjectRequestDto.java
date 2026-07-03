package com.nhnacademy.minidooraytask.project.domain;

public record ProjectRequestDto(
        String title,
        String description,
        ProjectStatus status
) {}
