package com.nhnacademy.minidooraytask.milestone.domain;

import java.util.List;

public record MilestoneListResponseDto (
    long projectId,
    List<MilestoneInfoResponseDto> milestoneList
) {
}
