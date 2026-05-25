package com.nhnacademy.minidooraytask.controller;

import com.nhnacademy.minidooraytask.milestone.domain.MilestoneRequestDto;
import com.nhnacademy.minidooraytask.milestone.service.MileStoneFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/task-api/tasks/{taskId}/milestones")
public class MileStoneController {
    private final MileStoneFacade mileStoneFacade;

    @PostMapping
    public ResponseEntity<Void> createMileStone(@PathVariable long taskId,
                                                @RequestHeader("X-Account-Id") long accountId,
                                                @RequestBody MilestoneRequestDto requestDto) {
        mileStoneFacade.createMilestone(taskId, accountId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateMileStone(@PathVariable long taskId,
                                                @RequestHeader("X-Account-Id") long accountId,
                                                @RequestBody MilestoneRequestDto requestDto) {
        mileStoneFacade.updateMilestone(taskId, accountId, requestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMileStone(@PathVariable long taskId,
                                                @RequestHeader("X-Account-Id") long accountId) {
        mileStoneFacade.deleteMilestone(taskId, accountId);
        return ResponseEntity.noContent().build();
    }
}