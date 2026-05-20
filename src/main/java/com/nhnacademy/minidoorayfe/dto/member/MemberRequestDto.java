package com.nhnacademy.minidoorayfe.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberRequestDto {
    private long accountId;
    private String userId;
    private MembersAuth auth;
}
