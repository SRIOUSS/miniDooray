package com.nhnacademy.minidoorayfe.dto.task;

import com.nhnacademy.minidoorayfe.dto.comment.CommentResponseDto;
import com.nhnacademy.minidoorayfe.dto.project.ProjectInfoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TaskViewDto {

    private TaskResponseDto taskResponseDto;
    private TaskInfoListDto taskInfoListDto;
    private ProjectInfoDto projectInfoDto;
    private List<CommentResponseDto> commentResponseDtoList;
}