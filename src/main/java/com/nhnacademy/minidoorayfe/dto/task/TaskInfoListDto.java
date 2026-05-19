package com.nhnacademy.minidoorayfe.dto.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskInfoListDto {

    private List<TaskInfoDto> taskInfoDtoList;
}
