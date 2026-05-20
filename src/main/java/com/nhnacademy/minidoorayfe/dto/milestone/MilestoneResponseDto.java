package com.nhnacademy.minidoorayfe.dto.milestone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Setter
public class MilestoneResponseDto {

    private long id;
    private String title;
    private String description;
    private MilestoneStatus status;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
