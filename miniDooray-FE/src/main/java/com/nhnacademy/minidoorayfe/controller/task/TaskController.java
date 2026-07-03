package com.nhnacademy.minidoorayfe.controller.task;

import com.nhnacademy.minidoorayfe.api.TaskApiClient;
import com.nhnacademy.minidoorayfe.dto.auth.SessionAccountDto;
import com.nhnacademy.minidoorayfe.dto.comment.CommentRequestDto;
import com.nhnacademy.minidoorayfe.dto.milestone.MilestoneRequestDto;
import com.nhnacademy.minidoorayfe.dto.task.TaskRequestDto;
import com.nhnacademy.minidoorayfe.dto.task.TaskViewDto;
import com.nhnacademy.minidoorayfe.resolver.SessionIdentity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/tasks")
public class TaskController {

    private final TaskApiClient taskApiClient;

    // 태스크 목록
    @GetMapping
    public String getTasks(@SessionIdentity SessionAccountDto sessionAccountDto,
                           @PathVariable Long projectId,
                           Model model) {

        model.addAttribute("projectId", projectId);
        model.addAttribute("tasks", this.taskApiClient.getTasks(projectId, sessionAccountDto.getAccountId()));

        return "task/list";
    }

    // 태스크 상세
    @GetMapping("/{taskId}")
    public String getTask(@SessionIdentity SessionAccountDto sessionAccountDto,
                          @PathVariable Long projectId,
                          @PathVariable Long taskId,
                          Model model) {

        TaskViewDto task = this.taskApiClient.getTask(projectId, taskId, sessionAccountDto.getAccountId());

        MilestoneRequestDto milestoneRequestDto = new MilestoneRequestDto();
        var ms = task.getTaskResponseDto().getMilestoneResponseDto();
        if (ms != null) {
            milestoneRequestDto.setTitle(ms.getTitle());
            milestoneRequestDto.setDescription(ms.getDescription());
            milestoneRequestDto.setStatus(ms.getStatus());
            milestoneRequestDto.setDueDate(ms.getDueDate());
        }

        model.addAttribute("projectId", projectId);
        model.addAttribute("taskId", taskId);
        model.addAttribute("task", task);
        model.addAttribute("allTasks", this.taskApiClient.getTasks(projectId, sessionAccountDto.getAccountId()));
        model.addAttribute("milestoneRequestDto", milestoneRequestDto);
        model.addAttribute("commentRequestDto", new CommentRequestDto());
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
    public String createTask(@SessionIdentity SessionAccountDto sessionAccountDto,
                             @PathVariable Long projectId,
                             @ModelAttribute TaskRequestDto dto) {

        this.taskApiClient.createTask(projectId, sessionAccountDto.getAccountId(), dto);

        return "redirect:/projects/{projectId}/tasks";
    }

    // 태스크 수정 폼
    @GetMapping("/{taskId}/edit")
    public String updateTaskForm(@SessionIdentity SessionAccountDto sessionAccountDto,
                                 @PathVariable Long projectId,
                                 @PathVariable Long taskId,
                                 Model model) {

        TaskViewDto task = taskApiClient.getTask(projectId, taskId, sessionAccountDto.getAccountId());

        String tagNames = task.getTaskResponseDto().getTagResponseDtoList() != null
                ? task.getTaskResponseDto().getTagResponseDtoList().stream()
                  .map(tag -> "#" + tag.getName().replaceAll("^#+", ""))
                  .collect(Collectors.joining(", "))
                : "";

        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTitle(task.getTaskResponseDto().getTitle());
        taskRequestDto.setContent(task.getTaskResponseDto().getContent());

        model.addAttribute("projectId", projectId);
        model.addAttribute("task", task);
        model.addAttribute("taskRequestDto", taskRequestDto);
        model.addAttribute("tagNames", tagNames);

        return "task/edit";
    }

    // 태스크 수정
    @PutMapping("/{taskId}")
    public String updateTask(@SessionIdentity SessionAccountDto sessionAccountDto,
                             @PathVariable Long projectId,
                             @PathVariable Long taskId,
                             @ModelAttribute TaskRequestDto dto) {

        this.taskApiClient.updateTask(projectId, taskId, sessionAccountDto.getAccountId(), dto);

        return "redirect:/projects/{projectId}/tasks/{taskId}";
    }

    // 태스크 삭제
    @DeleteMapping("/{taskId}")
    public String deleteTask(@SessionIdentity SessionAccountDto sessionAccountDto,
                             @PathVariable Long projectId,
                             @PathVariable Long taskId) {

        this.taskApiClient.deleteTask(projectId, taskId, sessionAccountDto.getAccountId());

        return "redirect:/projects/{projectId}/tasks";
    }
}
