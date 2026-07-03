package com.nhnacademy.minidoorayfe.dto.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ProjectRequestDto {

    private String title;
    private String description;
    private ProjectStatus status = ProjectStatus.ACTIVE; // // ACTIVE, DORMANT, TERMINATED
}
