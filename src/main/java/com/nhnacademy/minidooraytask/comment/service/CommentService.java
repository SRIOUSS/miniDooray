package com.nhnacademy.minidooraytask.comment.service;

import com.nhnacademy.minidooraytask.comment.domain.Comment;
import com.nhnacademy.minidooraytask.comment.domain.CommentRequestDto;
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
        if(commentRepository.existProjectMemberByTaskIdAndCommentIdANdAccountId(taskId, commentId, accountId)) {
            log.debug("[comment service] 존재하지 않는 댓글 수정입니다 - commentId:{}, taskId:{}, accountId:{}", commentId, taskId, accountId);
            throw new CommentNotFoundException("[comment service] 존재하지 않는 댓글입니다");
        }
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsByAccountId(Long accountId) {
        return commentRepository.findAllByProjectMember_AccountId(accountId);
    }

    //[댓글 생성]
    @Transactional
    public void createComment(Task task, ProjectMember member,  CommentRequestDto requestDto) {
        Comment comment = Comment.create(task, member, requestDto);

        commentRepository.save(comment);
    }

    //[댓글 수정]
    @Transactional
    public void updateComment(Long commentId,  CommentRequestDto requestDto) {

        // projectMember.getId()로 본인 댓글인지 확인
        Comment comment = commentRepository.findCommentById(commentId);

        comment.updateContent(requestDto.content());

        commentRepository.save(comment);
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
