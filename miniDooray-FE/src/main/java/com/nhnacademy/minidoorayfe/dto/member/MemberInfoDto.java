package com.nhnacademy.minidoorayfe.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MemberInfoDto {
    private long accountId;
    private long memberId;
    private String userId;
    private MembersAuth auth;
    private LocalDateTime joinedAt;
}
