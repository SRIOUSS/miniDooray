package com.nhnacademy.minidoorayfe.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoDto {
    private long accountId;
    private long memberId;
    private String userId;
    private MembersAuth auth;
    private LocalDateTime joinedAt;
}
