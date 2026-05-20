package com.nhnacademy.minidoorayfe.controller.task;

import com.nhnacademy.minidoorayfe.api.TaskApiClient;
import com.nhnacademy.minidoorayfe.dto.auth.SessionAccountDto;
import com.nhnacademy.minidoorayfe.resolver.SessionIdentity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {

    private final TaskApiClient taskApiClient;

    // 마이페이지에서 내가 작성한 Task 목록 (GET)
    @GetMapping("/tasks")
    public String getMyTasks(@SessionIdentity SessionAccountDto sessionAccountDto,
                             Model model) {

        model.addAttribute("tasks", this.taskApiClient.getMyTasks(sessionAccountDto.getAccountId()));

        return "mypage/tasks";
    }

    // 마이페이지에서 내가 작성한 Comment 목록 (GET)
    @GetMapping("/comments")
    public String getMyComments(@SessionIdentity SessionAccountDto sessionAccountDto,
                                Model model) {

        model.addAttribute("comments", this.taskApiClient.getMyComments(sessionAccountDto.getAccountId()));

        return "mypage/comments";
    }
}