package com.nhnacademy.minidoorayfe.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestDto {

    private String title;
    private String description;
    private ProjectStatus status; // // ACTIVE, DORMANT, TERMINATED
}
