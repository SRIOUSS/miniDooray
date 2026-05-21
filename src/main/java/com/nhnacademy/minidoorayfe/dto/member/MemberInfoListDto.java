package com.nhnacademy.minidoorayfe.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
public class MemberInfoListDto {
    private List<MemberInfoDto> memberInfoList = new ArrayList<>();
}

