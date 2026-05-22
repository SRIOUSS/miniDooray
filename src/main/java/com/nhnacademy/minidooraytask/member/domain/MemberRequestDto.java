package com.nhnacademy.minidooraytask.member.domain;

public record MemberRequestDto(
        Long accountId,
        String userId,
        MembersAuth auth
) {
    public Object a() {
        return null;
    }
}
