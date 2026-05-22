package com.nhnacademy.minidooraytask.project.domain;

import lombok.Getter;

//클라이언트가 직접 생성할 프로젝트의 제목과 설명
@Getter
public class ProjectCreateRequestDto {
    private String title;
    private String description;
}
