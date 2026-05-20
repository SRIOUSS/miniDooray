package com.nhnacademy.minidoorayfe.dto.project;

import com.nhnacademy.minidoorayfe.dto.task.TaskInfoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProjectViewDto {

    private List<ProjectInfoDto> projectInfoDtoList;
    private List<TaskInfoDto> taskInfoDtoList;
}
