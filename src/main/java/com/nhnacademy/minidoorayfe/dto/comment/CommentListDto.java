package com.nhnacademy.minidoorayfe.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentListDto {

    private List<CommentResponseDto> commentResponseDtoList;
}
