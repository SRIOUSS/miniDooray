package com.nhnacademy.minidoorayfe.dto.milestone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MilestoneRequestDto {

    private String title;
    private String description;
    private MilestoneStatus status;
    private LocalDateTime dueDate;
}
