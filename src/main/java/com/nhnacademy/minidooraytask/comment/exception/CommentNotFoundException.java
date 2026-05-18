package com.nhnacademy.minidooraytask.comment.exception;

//댓글이 존재하지 않을 때
public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String message) {
        super(message);
    }
}
