package com.nhnacademy.minidoorayfe.dto.member;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MembersAuth {
    MEMBER("사용자"),
    ADMIN("관리자");

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