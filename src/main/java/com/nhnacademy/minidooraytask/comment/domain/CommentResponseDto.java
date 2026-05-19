package com.nhnacademy.minidooraytask.comment.domain;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long id,
        Long accountId,
        String userId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
