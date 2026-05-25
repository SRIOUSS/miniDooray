package com.nhnacademy.minidooraytask.handler;

import com.nhnacademy.minidooraytask.comment.exception.CommentNotAuthorizedException;
import com.nhnacademy.minidooraytask.comment.exception.CommentNotFoundException;
import com.nhnacademy.minidooraytask.config.ErrorResponseDto;
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
import com.nhnacademy.minidooraytask.task.exception.TaskNotFoundException;
import com.nhnacademy.minidooraytask.task.exception.TaskValidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class CustomExceptionHandlerTest {

    private CustomExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CustomExceptionHandler();
    }

    @Test
    @DisplayName("404 - CommentNotFoundException")
    void handleNotFound_commentNotFound() {
        ResponseEntity<ErrorResponseDto> response = handler.handleNotFound(new CommentNotFoundException("not found"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().httpStatusCode()).isEqualTo(404);
        assertThat(response.getBody().message()).isEqualTo("not found");
    }

    @Test
    @DisplayName("404 - ProjectNotFoundException")
    void handleNotFound_projectNotFound() {
        ResponseEntity<ErrorResponseDto> response = handler.handleNotFound(new ProjectNotFoundException("pj not found"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("404 - TaskNotFoundException")
    void handleNotFound_taskNotFound() {
        ResponseEntity<ErrorResponseDto> response = handler.handleNotFound(new TaskNotFoundException("task not found"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("404 - UsernameNotFoundException")
    void handleNotFound_usernameNotFound() {
        ResponseEntity<ErrorResponseDto> response = handler.handleNotFound(new UsernameNotFoundException("user not found"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("404 - MileStoneIsNotExistException")
    void handleNotFound_milestoneNotExist() {
        ResponseEntity<ErrorResponseDto> response = handler.handleNotFound(new MileStoneIsNotExistException("ms not exist"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("404 - ProjectMemberIsNotExistException")
    void handleNotFound_memberNotExist() {
        ResponseEntity<ErrorResponseDto> response = handler.handleNotFound(new ProjectMemberIsNotExistException("member not exist"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("404 - tag.TagIsNotExistException")
    void handleNotFound_tagIsNotExist() {
        ResponseEntity<ErrorResponseDto> response = handler.handleNotFound(
                new com.nhnacademy.minidooraytask.tag.exception.TagIsNotExistException("tag not exist"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("404 - task.TagIsNotExistException")
    void handleNotFound_taskTagIsNotExist() {
        ResponseEntity<ErrorResponseDto> response = handler.handleNotFound(
                new com.nhnacademy.minidooraytask.task.exception.TagIsNotExistException("tag not exist"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("403 - CommentNotAuthorizedException")
    void handleForbidden_commentNotAuthorized() {
        ResponseEntity<ErrorResponseDto> response = handler.handleForbidden(new CommentNotAuthorizedException("forbidden"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody().httpStatusCode()).isEqualTo(403);
    }

    @Test
    @DisplayName("403 - NoAuthoProjectException")
    void handleForbidden_noAuthoProject() {
        ResponseEntity<ErrorResponseDto> response = handler.handleForbidden(new NoAuthoProjectException("no auth"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("409 - MileStoneIsExistException")
    void handleConflict_milestoneExist() {
        ResponseEntity<ErrorResponseDto> response = handler.handleConflict(new MileStoneIsExistException("conflict"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().httpStatusCode()).isEqualTo(409);
    }

    @Test
    @DisplayName("409 - AlreadyProjectMemberExistException")
    void handleConflict_memberExist() {
        ResponseEntity<ErrorResponseDto> response = handler.handleConflict(new AlreadyProjectMemberExistException("conflict"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("409 - tag.AlreadyTagExistException")
    void handleConflict_tagExist() {
        ResponseEntity<ErrorResponseDto> response = handler.handleConflict(new AlreadyTagExistException("conflict"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("409 - task.AlreadyTagExistException")
    void handleConflict_taskTagExist() {
        ResponseEntity<ErrorResponseDto> response = handler.handleConflict(
                new com.nhnacademy.minidooraytask.task.exception.AlreadyTagExistException("conflict"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("400 - MileStoneInvalidInputException")
    void handleBadRequest_milestoneInvalid() {
        ResponseEntity<ErrorResponseDto> response = handler.handleBadRequest(new MileStoneInvalidInputException("bad"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().httpStatusCode()).isEqualTo(400);
    }

    @Test
    @DisplayName("400 - ProjectMemberInvalidException")
    void handleBadRequest_memberInvalid() {
        ResponseEntity<ErrorResponseDto> response = handler.handleBadRequest(new ProjectMemberInvalidException("bad"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("400 - TaskValidInputException")
    void handleBadRequest_taskInvalid() {
        ResponseEntity<ErrorResponseDto> response = handler.handleBadRequest(new TaskValidInputException("bad"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("500 - 예상치 못한 예외")
    void handleServerError() {
        ResponseEntity<ErrorResponseDto> response = handler.handleServerError(new RuntimeException("oops"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().httpStatusCode()).isEqualTo(500);
    }
}