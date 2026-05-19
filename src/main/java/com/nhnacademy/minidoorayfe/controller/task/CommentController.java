package com.nhnacademy.minidoorayfe.controller.task;

import com.nhnacademy.minidoorayfe.api.TaskApiClient;
import com.nhnacademy.minidoorayfe.dto.auth.SessionAccountDto;
import com.nhnacademy.minidoorayfe.dto.comment.CommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/tasks/{taskId}/comments")
public class CommentController {

    private final TaskApiClient taskApiClient;

    private static final String REDIRECT_URL = "redirect:/projects/{projectId}/tasks/{taskId}";

    // 댓글 생성
    @PostMapping
    public String createComment(@ModelAttribute("sessionAccount")SessionAccountDto sessionAccountDto,
                                @PathVariable Long projectId,
                                @PathVariable Long taskId,
                                @ModelAttribute CommentRequestDto dto) {

        // projectId를 @PathVariable로 받고 있기는 하지만 TaskApiClient 호출에는 안 쓰임
        // 그러나 그대로 둬야 하는 이유가, 리다이렉트 URL에서 치환되어 쓰임으로 유지해야 함

        this.taskApiClient.createComment(taskId, sessionAccountDto.getAccountId(), dto);

        return REDIRECT_URL;
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public String updateComment(@ModelAttribute("sessionAccount") SessionAccountDto sessionAccountDto,
                                @PathVariable Long projectId,
                                @PathVariable Long taskId,
                                @PathVariable Long commentId,
                                @ModelAttribute CommentRequestDto dto) {

        this.taskApiClient.updateComment(taskId, sessionAccountDto.getAccountId(), commentId, dto);

        return REDIRECT_URL;
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public String deleteComment(@ModelAttribute("sessionAccount") SessionAccountDto sessionAccountDto,
                                @PathVariable Long projectId,
                                @PathVariable Long taskId,
                                @PathVariable Long commentId) {

        this.taskApiClient.deleteComment(taskId, sessionAccountDto.getAccountId(), commentId);

        return REDIRECT_URL;
    }
}
