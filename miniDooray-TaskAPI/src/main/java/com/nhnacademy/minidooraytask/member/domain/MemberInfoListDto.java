package com.nhnacademy.minidooraytask.member.domain;

import java.util.List;

public record MemberInfoListDto(
        List<MemberInfoDto> memberInfoDtoList
) {
}
