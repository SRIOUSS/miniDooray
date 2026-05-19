package com.nhnacademy.minidoorayfe.controller.task;

import com.nhnacademy.minidoorayfe.api.TaskApiClient;
import com.nhnacademy.minidoorayfe.dto.auth.SessionAccountDto;
import com.nhnacademy.minidoorayfe.dto.task.TaskRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/tasks")
public class TaskController {

    private final TaskApiClient taskApiClient;

    // 태스크 목록
    @GetMapping
    public String getTasks(@ModelAttribute("sessionAccount") SessionAccountDto sessionAccountDto,
                           @PathVariable Long projectId,
                           Model model) {

        model.addAttribute("projectId", projectId);
        model.addAttribute("tasks", this.taskApiClient.getTasks(projectId, sessionAccountDto.getAccountId()));

        return "task/list";
    }

    // 태스크 상세
    @GetMapping("/{taskId}")
    public String getTask(@ModelAttribute("sessionAccount") SessionAccountDto sessionAccountDto,
                          @PathVariable Long projectId,
                          @PathVariable Long taskId,
                          Model model) {

        model.addAttribute("projectId", projectId);
        model.addAttribute("task", this.taskApiClient.getTask(projectId, taskId, sessionAccountDto.getAccountId()));

        return "task/detail";
    }

    // 태스크 생성 폼
    @GetMapping("/new")
    public String createTaskForm(@PathVariable Long projectId,
                                 Model model) {

        model.addAttribute("projectId", projectId);
        model.addAttribute("taskRequestDto", new TaskRequestDto());

        return "task/form";
    }

    // 태스크 생성
    @PostMapping
    public String createTask(@ModelAttribute("sessionAccount") SessionAccountDto sessionAccountDto,
                             @PathVariable Long projectId,
                             @ModelAttribute TaskRequestDto dto) {

        this.taskApiClient.createTask(projectId, sessionAccountDto.getAccountId(), dto);

        return "redirect:/projects/{projectId}/tasks";
    }

    // 태스크 수정 폼
    @GetMapping("/{taskId}/edit")
    public String updateTaskForm(@ModelAttribute("sessionAccount") SessionAccountDto sessionAccountDto,
                                 @PathVariable Long projectId,
                                 @PathVariable Long taskId,
                                 Model model) {

        model.addAttribute("projectId", projectId);
        model.addAttribute("task", this.taskApiClient.getTask(projectId, taskId, sessionAccountDto.getAccountId()));
        model.addAttribute("taskRequestDto", new TaskRequestDto());

        return "task/edit";
    }

    // 태스크 수정
    @PutMapping("/{taskId}")
    public String updateTask(@ModelAttribute("sessionAccount") SessionAccountDto sessionAccountDto,
                             @PathVariable Long projectId,
                             @PathVariable Long taskId,
                             @ModelAttribute TaskRequestDto dto) {

        this.taskApiClient.updateTask(projectId, taskId, sessionAccountDto.getAccountId(), dto);

        return "redirect:/projects/{projectId}/tasks/{taskId}";
    }

    // 태스크 삭제
    @DeleteMapping("/{taskId}")
    public String deleteTask(@ModelAttribute("sessionAccount") SessionAccountDto sessionAccountDto,
                             @PathVariable Long projectId,
                             @PathVariable Long taskId) {

        this.taskApiClient.deleteTask(projectId, taskId, sessionAccountDto.getAccountId());

        return "redirect:/projects/{projectId}/tasks";
    }
}
