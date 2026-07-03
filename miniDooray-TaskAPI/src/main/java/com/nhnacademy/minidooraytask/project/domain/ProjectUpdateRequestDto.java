package com.nhnacademy.minidooraytask.project.domain;

public record ProjectUpdateRequestDto(
        String title,
        String description,
        ProjectStatus status
) {}
