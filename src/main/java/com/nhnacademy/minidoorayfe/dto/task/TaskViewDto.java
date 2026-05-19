package com.nhnacademy.minidoorayfe.dto.task;

import com.nhnacademy.minidooraygateway.dto.project.ProjectInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskViewDto {

    private TaskResponseDto taskResponseDto;
    private TaskInfoListDto taskInfoListDto;
    private ProjectInfoDto projectInfoDto;
}
