package com.nhnacademy.minidooraytask.comment.domain;

import java.time.LocalDateTime;

public record CommentResponseDto(
        long commentId,
        long taskId,
        long memberId,
        String content,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
}
