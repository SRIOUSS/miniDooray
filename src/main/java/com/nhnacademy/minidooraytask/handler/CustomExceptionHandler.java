package com.nhnacademy.minidooraytask.handler;

import com.nhnacademy.minidooraytask.comment.exception.CommentNotAuthorizedException;
import com.nhnacademy.minidooraytask.comment.exception.CommentNotFoundException;
import com.nhnacademy.minidooraytask.config.exception.UsernameNotFoundException;
import com.nhnacademy.minidooraytask.member.exception.ProjectMemberIsNotExistException;
import com.nhnacademy.minidooraytask.project.exception.NoAuthoProjectException;
import com.nhnacademy.minidooraytask.project.exception.ProjectNotFoundException;
import com.nhnacademy.minidooraytask.task.exception.TagIsNotExistException;
import com.nhnacademy.minidooraytask.task.exception.TaskNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    //찾지 못하는 예외
    @ExceptionHandler({
            CommentNotFoundException.class,
            ProjectNotFoundException.class,
            TaskNotFoundException.class,
            UsernameNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleException1(Exception ex) {
        return ResponseEntity.badRequest().build();
    }

    //권한이 없거나 존재하지 않을 떄
    @ExceptionHandler({
            CommentNotAuthorizedException.class,
            ProjectMemberIsNotExistException.class,
            NoAuthoProjectException.class,
            TagIsNotExistException.class
    })
    public ResponseEntity<ErrorResponse> handleException2(Exception ex) {
        return ResponseEntity.badRequest().build();
    }

    // 나머지
    @ExceptionHandler(Exception.class)
     public ResponseEntity<ErrorResponse> handleException3(Exception ex) {
        return ResponseEntity.badRequest().build();
    }
}