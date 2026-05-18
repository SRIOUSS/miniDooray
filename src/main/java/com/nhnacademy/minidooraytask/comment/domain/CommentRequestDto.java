package com.nhnacademy.minidooraytask.comment.domain;

public record CommentRequestDto(
//        long memberId  memberId는 헤더에서 받아 서비스에서 처리하기
        String content
) {
}
