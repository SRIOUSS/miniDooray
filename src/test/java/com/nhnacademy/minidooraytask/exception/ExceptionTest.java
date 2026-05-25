package com.nhnacademy.minidooraytask.exception;

import com.nhnacademy.minidooraytask.comment.exception.CommentNotAuthorizedException;
import com.nhnacademy.minidooraytask.comment.exception.CommentNotFoundException;
import com.nhnacademy.minidooraytask.config.exception.UsernameNotFoundException;
import com.nhnacademy.minidooraytask.member.exception.AlreadyProjectMemberExistException;
import com.nhnacademy.minidooraytask.member.exception.ProjectMemberInvalidException;
import com.nhnacademy.minidooraytask.member.exception.ProjectMemberIsNotExistException;
import com.nhnacademy.minidooraytask.milestone.exception.MileStoneInvalidInputException;
import com.nhnacademy.minidooraytask.milestone.exception.MileStoneIsExistException;
import com.nhnacademy.minidooraytask.milestone.exception.MileStoneIsNotExistException;
import com.nhnacademy.minidooraytask.project.exception.NoAuthoProjectException;
import com.nhnacademy.minidooraytask.project.exception.ProjectNotFoundException;
import com.nhnacademy.minidooraytask.tag.exception.AlreadyTagExistException;
import com.nhnacademy.minidooraytask.tag.exception.TagIsNotExistException;
import com.nhnacademy.minidooraytask.task.exception.TaskNotFoundException;
import com.nhnacademy.minidooraytask.task.exception.TaskValidInputException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionTest {

    @Test
    void commentNotAuthorizedException() {
        CommentNotAuthorizedException ex = new CommentNotAuthorizedException("unauthorized");
        assertThat(ex.getMessage()).isEqualTo("unauthorized");
    }

    @Test
    void commentNotFoundException() {
        CommentNotFoundException ex = new CommentNotFoundException("not found");
        assertThat(ex.getMessage()).isEqualTo("not found");
    }

    @Test
    void usernameNotFoundException() {
        UsernameNotFoundException ex = new UsernameNotFoundException("no user");
        assertThat(ex.getMessage()).isEqualTo("no user");
    }

    @Test
    void alreadyProjectMemberExistException() {
        AlreadyProjectMemberExistException ex = new AlreadyProjectMemberExistException("already exists");
        assertThat(ex.getMessage()).isEqualTo("already exists");
    }

    @Test
    void projectMemberInvalidException() {
        ProjectMemberInvalidException ex = new ProjectMemberInvalidException("invalid");
        assertThat(ex.getMessage()).isEqualTo("invalid");
    }

    @Test
    void projectMemberIsNotExistException() {
        ProjectMemberIsNotExistException ex = new ProjectMemberIsNotExistException("not exist");
        assertThat(ex.getMessage()).isEqualTo("not exist");
    }

    @Test
    void mileStoneInvalidInputException() {
        MileStoneInvalidInputException ex = new MileStoneInvalidInputException("invalid input");
        assertThat(ex.getMessage()).isEqualTo("invalid input");
    }

    @Test
    void mileStoneIsExistException() {
        MileStoneIsExistException ex = new MileStoneIsExistException("already exist");
        assertThat(ex.getMessage()).isEqualTo("already exist");
    }

    @Test
    void mileStoneIsNotExistException() {
        MileStoneIsNotExistException ex = new MileStoneIsNotExistException("not exist");
        assertThat(ex.getMessage()).isEqualTo("not exist");
    }

    @Test
    void noAuthoProjectException() {
        NoAuthoProjectException ex = new NoAuthoProjectException("no auth");
        assertThat(ex.getMessage()).isEqualTo("no auth");
    }

    @Test
    void projectNotFoundException() {
        ProjectNotFoundException ex = new ProjectNotFoundException("not found");
        assertThat(ex.getMessage()).isEqualTo("not found");
    }

    @Test
    void alreadyTagExistException() {
        AlreadyTagExistException ex = new AlreadyTagExistException("tag exists");
        assertThat(ex.getMessage()).isEqualTo("tag exists");
    }

    @Test
    void tagIsNotExistException() {
        TagIsNotExistException ex = new TagIsNotExistException("tag missing");
        assertThat(ex.getMessage()).isEqualTo("tag missing");
    }

    @Test
    void taskAlreadyTagExistException() {
        com.nhnacademy.minidooraytask.task.exception.AlreadyTagExistException ex =
                new com.nhnacademy.minidooraytask.task.exception.AlreadyTagExistException("tag exists");
        assertThat(ex.getMessage()).isEqualTo("tag exists");
    }

    @Test
    void taskTagIsNotExistException() {
        com.nhnacademy.minidooraytask.task.exception.TagIsNotExistException ex =
                new com.nhnacademy.minidooraytask.task.exception.TagIsNotExistException("tag missing");
        assertThat(ex.getMessage()).isEqualTo("tag missing");
    }

    @Test
    void taskNotFoundException() {
        TaskNotFoundException ex = new TaskNotFoundException("task missing");
        assertThat(ex.getMessage()).isEqualTo("task missing");
    }

    @Test
    void taskValidInputException() {
        TaskValidInputException ex = new TaskValidInputException("invalid task");
        assertThat(ex.getMessage()).isEqualTo("invalid task");
    }
}