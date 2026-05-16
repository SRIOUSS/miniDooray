package com.nhnacademy.minidooraytask.comment.domain;

public record CommentRequestDto(
        long memberId,
        String content
) {
}
