package com.nhnacademy.minidooraytask.milestone.domain;

public interface MileStoneListProjection {
    Long getProjectId();
    Long getMileStoneId();
    MileStoneStatus getStatus();
}
