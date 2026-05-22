package com.nhnacademy.minidooraytask.member.domain;

import java.time.LocalDateTime;

public record MemberInfoDto(
        long accountId,
        long memberId,
        String userId,
        MembersAuth auth,
        LocalDateTime joinedAt
) {
}
