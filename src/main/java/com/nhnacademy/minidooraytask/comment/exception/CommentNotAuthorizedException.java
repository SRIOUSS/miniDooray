package com.nhnacademy.minidooraytask.comment.exception;

//본인 뎃글이 아닐 때
public class CommentNotAuthorizedException extends RuntimeException {
    public CommentNotAuthorizedException(String message) {
        super(message);
    }
}
