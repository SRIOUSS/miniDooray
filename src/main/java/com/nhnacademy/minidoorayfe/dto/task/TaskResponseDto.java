package com.nhnacademy.minidoorayfe.dto.task;

import com.nhnacademy.minidooraygateway.dto.milestone.MilestoneResponseDto;
import com.nhnacademy.minidooraygateway.dto.tag.TagResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto {

    private long taskId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TagResponseDto> tagResponseDtoList;
    private MilestoneResponseDto milestoneResponseDto;
}
