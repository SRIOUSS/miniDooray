package com.nhnacademy.minidooraytask.project.domain;

import com.nhnacademy.minidooraytask.MileStone.domain.MileStoneStatus;

import java.util.List;

public record ProjectInfoDto(
        long id,
        String title,
        ProjectStatus status,
        List<MileStoneStatus> taskStatusList
) {
}
