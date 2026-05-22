package com.nhnacademy.minidoorayfe.exception;

public class ApiServerException extends RuntimeException {
    public ApiServerException(String message) {
        super(message);
    }
}