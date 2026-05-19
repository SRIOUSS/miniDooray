package com.nhnacademy.minidoorayfe.dto.milestone;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneRequestDto {

    private String title;
    private String description;
    private MilestoneStatus status;
    private LocalDateTime dueDate;
}
