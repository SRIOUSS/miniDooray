package com.nhnacademy.minidoorayfe.dto.project;

import com.nhnacademy.minidooraygateway.dto.milestone.MilestoneStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfoDto {

    private long id;
    private String title;
    private ProjectStatus status;
    private List<MilestoneStatus> taskStatusList;
}
