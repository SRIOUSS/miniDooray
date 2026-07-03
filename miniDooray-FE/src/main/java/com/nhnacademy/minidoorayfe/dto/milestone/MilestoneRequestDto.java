package com.nhnacademy.minidoorayfe.dto.milestone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MilestoneRequestDto {

    private String title;
    private String description;
    private MilestoneStatus status;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dueDate;
}
