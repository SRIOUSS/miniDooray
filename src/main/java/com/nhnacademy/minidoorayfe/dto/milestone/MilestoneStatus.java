package com.nhnacademy.minidoorayfe.dto.milestone;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MilestoneStatus {
    PLANNED("예정"),
    IN_PROGRESS("진행 중"),
    COMPLETED("완료"),
    CANCELLED("취소됨");

    private final String displayName;

    @Override
    public String toString() {
        return this.displayName;
    }
}