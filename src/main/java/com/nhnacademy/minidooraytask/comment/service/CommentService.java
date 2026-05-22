package com.nhnacademy.minidooraytask.comment.service;

import com.nhnacademy.minidooraytask.comment.domain.Comment;
import com.nhnacademy.minidooraytask.comment.domain.CommentRequestDto;
import com.nhnacademy.minidooraytask.comment.exception.CommentNotAuthorizedException;
import com.nhnacademy.minidooraytask.comment.exception.CommentNotFoundException;
import com.nhnacademy.minidooraytask.comment.repository.CommentRepository;
import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import com.nhnacademy.minidooraytask.task.domain.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public void checkTaskComment(long taskId, long commentId, long accountId) {
        // 1. 댓글이 해당 task에 속하는지 확인
        Comment comment = commentRepository.findByIdAndTask_Id(commentId, taskId)
                .orElseThrow(() -> {
                    log.debug("[comment service] 존재하지 않는 댓글입니다 - commentId:{}, taskId:{}", commentId, taskId);
                    return new CommentNotFoundException("[comment service] 존재하지 않는 댓글입니다");
                });

        // 2. 댓글 작성자인지 확인
        if (!comment.getProjectMember().getAccountId().equals(accountId)) {
            log.debug("[comment service] 댓글 수정/삭제 권한이 없습니다 - commentId:{}, accountId:{}", commentId, accountId);
            throw new CommentNotAuthorizedException("[comment service] 댓글 수정/삭제 권한이 없습니다");
        }
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsByAccountId(Long accountId) {
        return commentRepository.findAllByProjectMember_AccountId(accountId);
    }

    @Transactional
    public void createComment(Task task, ProjectMember member, CommentRequestDto requestDto) {
        Comment comment = Comment.create(task, member, requestDto);
        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long commentId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findCommentById(commentId);
        comment.updateContent(requestDto.content());
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}