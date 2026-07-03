package com.nhnacademy.minidoorayfe.dto.task;

import com.nhnacademy.minidoorayfe.dto.milestone.MilestoneStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskInfoDto {

    private long id;
    private String title;
    private MilestoneStatus status;
}
