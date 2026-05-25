package com.nhnacademy.minidooraytask.milestone.service;

import com.nhnacademy.minidooraytask.milestone.domain.MilestoneRequestDto;
import com.nhnacademy.minidooraytask.task.domain.Task;
import com.nhnacademy.minidooraytask.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class MileStoneFacade {
    private final MileStoneService mileStoneService;
    private final TaskService taskService;

    @Transactional
    public void createMilestone(long taskId, long accountId, MilestoneRequestDto requestDto) {
        taskService.checkTaskMakerByAccountId(accountId, taskId);

        Task task = taskService.getTaskById(taskId);

        mileStoneService.createMileStone(task, requestDto);
    }

    @Transactional
    public void updateMilestone(long taskId, long accountId, MilestoneRequestDto requestDto) {
        taskService.checkTaskMakerByAccountId(accountId, taskId);

        mileStoneService.updateMileStone(taskId, requestDto);
    }

    @Transactional
    public void deleteMilestone(long taskId, long accountId) {
        taskService.checkTaskMakerByAccountId(accountId, taskId);

        mileStoneService.deleteMileStone(taskId);
    }
}
