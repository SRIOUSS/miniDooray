package com.nhnacademy.minidoorayfe.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentListDto {

    @JsonProperty("commentResponseList")
    private List<CommentResponseDto> commentResponseDtoList;
}
