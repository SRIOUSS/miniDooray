package com.nhnacademy.minidoorayfe.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoListDto {
    private List<MemberInfoDto> memberInfoList;
}

