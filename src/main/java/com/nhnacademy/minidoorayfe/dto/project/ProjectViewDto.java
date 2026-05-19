package com.nhnacademy.minidoorayfe.dto.project;

import com.nhnacademy.minidoorayfe.dto.task.TaskInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectViewDto {

    private List<ProjectInfoDto> projectInfoDtoList;
    private List<TaskInfoDto> taskInfoDtoList;
}
