package com.nhnacademy.minidooraytask.MileStone.domain;

import java.util.List;

public record MilestoneListResponseDto (
    long projectId,
    List<MilestoneInfoResponseDto> milestoneList
) {
}
