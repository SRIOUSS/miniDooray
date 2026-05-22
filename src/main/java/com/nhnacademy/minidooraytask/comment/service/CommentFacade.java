package com.nhnacademy.minidooraytask.comment.service;

import com.nhnacademy.minidooraytask.comment.domain.Comment;
import com.nhnacademy.minidooraytask.comment.domain.CommentListDto;
import com.nhnacademy.minidooraytask.comment.domain.CommentRequestDto;
import com.nhnacademy.minidooraytask.comment.domain.CommentResponseDto;
import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import com.nhnacademy.minidooraytask.member.service.ProjectMemberService;
import com.nhnacademy.minidooraytask.task.domain.Task;
import com.nhnacademy.minidooraytask.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CommentFacade {
    private final CommentService commentService;
    private final ProjectMemberService memberService;
    private final TaskService taskService;

    @Transactional
    public void createComment(long taskId, long accountId, CommentRequestDto requestDto) {
        taskService.checkProjectMember(taskId, accountId);

        Task task = taskService.getTaskById(taskId);
        ProjectMember member = memberService.getMemberByTaskId(taskId, accountId);

        commentService.createComment(task, member, requestDto);
    }

    @Transactional
    public CommentListDto getCommentList(long accountId) {
        List<Comment> commentList = commentService.getCommentsByAccountId(accountId);

        String userId = memberService.getUserIdByAccountId(accountId);

        List<CommentResponseDto> commentResponseList = commentList.stream()
                .map(c ->
                        new CommentResponseDto(c.getId(), accountId, userId, c.getContent(),
                                c.getCreatedAt(), c.getUpdatedAt()))
                .toList();

        return new CommentListDto(commentResponseList);
    }

    @Transactional
    public void updateComment(long taskId, long commentId, long accountId, CommentRequestDto requestDto) {
        commentService.checkTaskComment(taskId, commentId, accountId);

        commentService.updateComment(commentId, requestDto);
    }

    @Transactional
    public void deleteComment(long taskId, long commentId, long accountId) {
        commentService.checkTaskComment(taskId, commentId, accountId);

        commentService.deleteComment(commentId);
    }
}
