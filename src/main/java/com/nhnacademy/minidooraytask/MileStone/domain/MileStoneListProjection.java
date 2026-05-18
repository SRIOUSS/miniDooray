package com.nhnacademy.minidooraytask.MileStone.domain;

public interface MileStoneListProjection {
    Long getProjectId();
    Long getMileStoneId();
    MileStoneStatus getStatus();
}
