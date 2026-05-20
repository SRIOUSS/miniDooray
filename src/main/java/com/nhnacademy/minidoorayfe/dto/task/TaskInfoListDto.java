package com.nhnacademy.minidoorayfe.dto.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TaskInfoListDto {

    private List<TaskInfoDto> taskInfoDtoList;
}
