package com.nhnacademy.minidoorayfe.controller.task;

import com.nhnacademy.minidoorayfe.api.TaskApiClient;
import com.nhnacademy.minidoorayfe.dto.auth.SessionAccountDto;
import com.nhnacademy.minidoorayfe.dto.milestone.MilestoneRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/tasks/{taskId}/milestones")
public class MilestoneController {

    private final TaskApiClient taskApiClient;

    private static final String REDIRECT_URL = "redirect:/projects/{projectId}/tasks/{taskId}";

    // 마일스톤 생성
    @PostMapping
    public String createMilestone(@ModelAttribute("sessionAccount")SessionAccountDto sessionAccountDto,
                                  @PathVariable Long projectId,
                                  @PathVariable Long taskId,
                                  @ModelAttribute MilestoneRequestDto dto) {

        this.taskApiClient.createMilestone(taskId, sessionAccountDto.getAccountId(), dto);

        return REDIRECT_URL;
    }

    // 마일스톤 수정
    @PutMapping
    public String updateMilestone(@ModelAttribute("sessionAccount") SessionAccountDto sessionAccountDto,
                                  @PathVariable Long projectId,
                                  @PathVariable Long taskId,
                                  @ModelAttribute MilestoneRequestDto dto) {

        this.taskApiClient.updateMilestone(taskId, sessionAccountDto.getAccountId(), dto);

        return REDIRECT_URL;
    }

    // 마일스톤 삭제
    @DeleteMapping
    public String deleteMilestone(@ModelAttribute("sessionAccount") SessionAccountDto sessionAccountDto,
                                  @PathVariable Long projectId,
                                  @PathVariable Long taskId) {

        this.taskApiClient.deleteMilestone(taskId, sessionAccountDto.getAccountId());

        return REDIRECT_URL;
    }
}