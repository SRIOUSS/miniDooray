package com.nhnacademy.minidoorayfe.dto.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TaskRequestDto {

    private String title;
    private String content;
    private List<String> tagNameList;
}
