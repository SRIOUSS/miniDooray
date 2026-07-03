package com.nhnacademy.minidoorayfe.dto.project;

import com.nhnacademy.minidoorayfe.dto.milestone.MilestoneStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProjectInfoDto {

    private long id;
    private String title;
    private ProjectStatus status;
    private List<MilestoneStatus> taskStatusList;
}
