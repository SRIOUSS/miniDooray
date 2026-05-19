package com.nhnacademy.minidoorayfe.dto.task;

import com.nhnacademy.minidoorayfe.dto.comment.CommentResponseDto;
import com.nhnacademy.minidoorayfe.dto.project.ProjectInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskViewDto {

    private TaskResponseDto taskResponseDto;
    private TaskInfoListDto taskInfoListDto;
    private ProjectInfoDto projectInfoDto;
    private List<CommentResponseDto> commentResponseDtoList;
}