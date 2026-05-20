package com.nhnacademy.minidoorayfe.dto.task;

import com.nhnacademy.minidoorayfe.dto.milestone.MilestoneResponseDto;
import com.nhnacademy.minidoorayfe.dto.tag.TagResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TaskResponseDto {

    private long taskId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TagResponseDto> tagResponseDtoList;
    private MilestoneResponseDto milestoneResponseDto;
}
