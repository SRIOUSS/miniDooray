package com.nhnacademy.minidoorayfe.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {
    private long accountId;
    private String userId;
    private MembersAuth auth;
}
