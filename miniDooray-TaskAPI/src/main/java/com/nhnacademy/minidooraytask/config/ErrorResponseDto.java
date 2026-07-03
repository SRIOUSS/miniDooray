package com.nhnacademy.minidooraytask.config;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        int httpStatusCode,
        LocalDateTime errorTime
) {}
