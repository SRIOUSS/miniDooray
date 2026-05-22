package com.nhnacademy.minidooraytask.handler;

import com.nhnacademy.minidooraytask.MileStone.exception.MileStoneInvalidInputException;
import com.nhnacademy.minidooraytask.MileStone.exception.MileStoneIsExistException;
import com.nhnacademy.minidooraytask.MileStone.exception.MileStoneIsNotExistException;
import com.nhnacademy.minidooraytask.comment.exception.CommentNotAuthorizedException;
import com.nhnacademy.minidooraytask.comment.exception.CommentNotFoundException;
import com.nhnacademy.minidooraytask.config.ErrorResponseDto;
import com.nhnacademy.minidooraytask.config.exception.UsernameNotFoundException;
import com.nhnacademy.minidooraytask.member.exception.AlreadyProjectMemberExistException;
import com.nhnacademy.minidooraytask.member.exception.ProjectMemberInvalidException;
import com.nhnacademy.minidooraytask.member.exception.ProjectMemberIsNotExistException;
import com.nhnacademy.minidooraytask.project.exception.NoAuthoProjectException;
import com.nhnacademy.minidooraytask.project.exception.ProjectNotFoundException;
import com.nhnacademy.minidooraytask.tag.exception.AlreadyTagExistException;
import com.nhnacademy.minidooraytask.task.exception.TaskNotFoundException;
import com.nhnacademy.minidooraytask.task.exception.TaskValidInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    // 404 Not Found
    @ExceptionHandler({
            CommentNotFoundException.class,
            ProjectNotFoundException.class,
            TaskNotFoundException.class,
            UsernameNotFoundException.class,
            MileStoneIsNotExistException.class,
            ProjectMemberIsNotExistException.class,
            com.nhnacademy.minidooraytask.tag.exception.TagIsNotExistException.class,
            com.nhnacademy.minidooraytask.task.exception.TagIsNotExistException.class
    })
    public ResponseEntity<ErrorResponseDto> handleNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(ex.getMessage(), 404, LocalDateTime.now()));
    }

    // 403 Forbidden
    @ExceptionHandler({
            CommentNotAuthorizedException.class,
            NoAuthoProjectException.class
    })
    public ResponseEntity<ErrorResponseDto> handleForbidden(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto(ex.getMessage(), 403, LocalDateTime.now()));
    }

    // 409 Conflict
    @ExceptionHandler({
            MileStoneIsExistException.class,
            AlreadyProjectMemberExistException.class,
            AlreadyTagExistException.class,
            com.nhnacademy.minidooraytask.task.exception.AlreadyTagExistException.class
    })
    public ResponseEntity<ErrorResponseDto> handleConflict(Exception ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDto(ex.getMessage(), 409, LocalDateTime.now()));
    }

    // 400 Bad Request
    @ExceptionHandler({
            MileStoneInvalidInputException.class,
            ProjectMemberInvalidException.class,
            TaskValidInputException.class
    })
    public ResponseEntity<ErrorResponseDto> handleBadRequest(Exception ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto(ex.getMessage(), 400, LocalDateTime.now()));
    }

    // 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleServerError(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto("서버 내부 오류가 발생했습니다.", 500, LocalDateTime.now()));
    }
}