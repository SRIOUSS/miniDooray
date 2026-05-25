package com.nhnacademy.minidooraytask.service;

import com.nhnacademy.minidooraytask.comment.domain.Comment;
import com.nhnacademy.minidooraytask.comment.domain.CommentRequestDto;
import com.nhnacademy.minidooraytask.comment.exception.CommentNotAuthorizedException;
import com.nhnacademy.minidooraytask.comment.exception.CommentNotFoundException;
import com.nhnacademy.minidooraytask.comment.repository.CommentRepository;
import com.nhnacademy.minidooraytask.comment.service.CommentService;
import com.nhnacademy.minidooraytask.member.domain.MembersAuth;
import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import com.nhnacademy.minidooraytask.project.domain.Project;
import com.nhnacademy.minidooraytask.task.domain.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Test
    @DisplayName("댓글이 존재하지 않으면 CommentNotFoundException 발생")
    void checkTaskComment_fail_notFound() {
        long taskId = 1L;
        long commentId = 100L;
        long accountId = 999L;

        given(commentRepository.findByIdAndTask_Id(commentId, taskId))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.checkTaskComment(taskId, commentId, accountId))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    @DisplayName("댓글 작성자가 다르면 CommentNotAuthorizedException 발생")
    void checkTaskComment_fail_notAuthorized() {
        long taskId = 1L;
        long commentId = 100L;
        long accountId = 999L;

        Project project = new Project("t", "d", 1L);
        ProjectMember member = new ProjectMember(project, 200L, MembersAuth.MEMBER);
        Task task = new Task(project, member, "태스크", "내용");
        Comment comment = new Comment(task, member, "댓글내용");

        given(commentRepository.findByIdAndTask_Id(commentId, taskId))
                .willReturn(Optional.of(comment));

        assertThatThrownBy(() -> commentService.checkTaskComment(taskId, commentId, accountId))
                .isInstanceOf(CommentNotAuthorizedException.class);
    }

    @Test
    @DisplayName("댓글 작성자 확인 - 성공")
    void checkTaskComment_success() {
        long taskId = 1L;
        long commentId = 100L;
        long accountId = 200L;

        Project project = new Project("t", "d", 1L);
        ProjectMember member = new ProjectMember(project, accountId, MembersAuth.MEMBER);
        Task task = new Task(project, member, "태스크", "내용");
        Comment comment = new Comment(task, member, "댓글내용");

        given(commentRepository.findByIdAndTask_Id(commentId, taskId))
                .willReturn(Optional.of(comment));

        commentService.checkTaskComment(taskId, commentId, accountId);
    }

    @Test
    @DisplayName("accountId로 댓글 목록 조회")
    void getCommentsByAccountId_success() {
        Project project = new Project("t", "d", 1L);
        ProjectMember member = new ProjectMember(project, 100L, MembersAuth.MEMBER);
        Task task = new Task(project, member, "태스크", "내용");
        Comment comment = new Comment(task, member, "댓글내용");

        given(commentRepository.findAllByProjectMember_AccountId(100L)).willReturn(List.of(comment));

        List<Comment> result = commentService.getCommentsByAccountId(100L);
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("댓글 생성 성공")
    void createComment_success() {
        Project project = new Project("t", "d", 1L);
        ProjectMember member = new ProjectMember(project, 100L, MembersAuth.MEMBER);
        Task task = new Task(project, member, "태스크", "내용");
        CommentRequestDto dto = new CommentRequestDto("새댓글");

        given(commentRepository.save(any(Comment.class))).willReturn(new Comment(task, member, "새댓글"));

        commentService.createComment(task, member, dto);

        then(commentRepository).should().save(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void updateComment_success() {
        Project project = new Project("t", "d", 1L);
        ProjectMember member = new ProjectMember(project, 100L, MembersAuth.MEMBER);
        Task task = new Task(project, member, "태스크", "내용");
        Comment comment = new Comment(task, member, "기존댓글");

        given(commentRepository.findCommentById(1L)).willReturn(comment);
        given(commentRepository.save(comment)).willReturn(comment);

        commentService.updateComment(1L, new CommentRequestDto("수정댓글"));

        assertThat(comment.getContent()).isEqualTo("수정댓글");
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteComment_success() {
        commentService.deleteComment(1L);
        then(commentRepository).should().deleteById(1L);
    }
}