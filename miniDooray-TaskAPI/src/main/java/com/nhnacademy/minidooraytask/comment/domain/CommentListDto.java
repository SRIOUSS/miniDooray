package com.nhnacademy.minidooraytask.comment.domain;

import java.util.List;

public record CommentListDto(
        List<CommentResponseDto> commentResponseList
) {}
