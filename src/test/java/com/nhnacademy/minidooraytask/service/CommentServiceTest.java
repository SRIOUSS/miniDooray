package com.nhnacademy.minidooraytask.service;

import com.nhnacademy.minidooraytask.comment.exception.CommentNotFoundException;
import com.nhnacademy.minidooraytask.comment.repository.CommentRepository;
import com.nhnacademy.minidooraytask.comment.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Test
    @DisplayName("댓글이 존재하지 않으면 CommentNotFoundException 발생")
    void checkTaskComment_fail_notAuthorized() {
        long taskId = 1L;
        long commentId = 100L;
        long accountId = 999L;

        given(commentRepository.findByIdAndTask_Id(commentId, taskId))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.checkTaskComment(taskId, commentId, accountId))
                .isInstanceOf(CommentNotFoundException.class);
    }
}