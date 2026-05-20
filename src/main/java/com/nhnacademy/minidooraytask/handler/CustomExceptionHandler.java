package com.nhnacademy.minidooraytask.handler;

import com.nhnacademy.minidooraytask.comment.exception.CommentNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    //찾지 못하는 예외
    @ExceptionHandler({CommentNotFoundException.class, }) {

    }

    //권한이 없거나 존재하지 않을 떄
    @ExceptionHandler()
}
