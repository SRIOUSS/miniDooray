package com.nhnacademy.minidoorayfe.dto.task;

import com.nhnacademy.minidooraygateway.dto.milestone.MilestoneStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskInfoDto {

    private long id;
    private String title;
    private MilestoneStatus status;
}
