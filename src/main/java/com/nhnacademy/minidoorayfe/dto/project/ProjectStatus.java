package com.nhnacademy.minidoorayfe.dto.project;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectStatus {
    ACTIVE("활성"),
    DORMANT("휴면"),
    TERMINATED("종료");

    private final String displayName;

    @Override
    public String toString() {
        return this.displayName;
    }

    @JsonValue
    public String getValue() {
        return this.name();
    }
}
