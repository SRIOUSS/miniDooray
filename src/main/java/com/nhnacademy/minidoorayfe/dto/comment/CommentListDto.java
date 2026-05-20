package com.nhnacademy.minidoorayfe.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentListDto {

    private List<CommentResponseDto> commentResponseDtoList;
}
