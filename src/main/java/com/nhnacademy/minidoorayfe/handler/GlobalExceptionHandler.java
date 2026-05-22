package com.nhnacademy.minidoorayfe.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;

@ControllerAdvice // 모든 컨트롤러에서 발생하는 예외 전역으로 잡아줌
@Slf4j
public class GlobalExceptionHandler {

    /*
    게이트웨이가 FE RestClient로 400 전달
    -> RestClientConfig의 defaultStatusHandler가 400 감지함
    -> RestClientException 던짐
    -> 예외가 컨트롤러까지 전파
    -> @ControllerAdvice GlobalExceptionHandler가 RestClientException 감지
    -> handleRestClientException 실행함
    -> error/400
     */

    // RestClientException (Gateway API 호출 실패)
    @ExceptionHandler(RestClientException.class)
    public String handleRestClientException(RestClientException e, Model model) {
        log.error("RestClientException: {}", e.getMessage());
        model.addAttribute("message", e.getMessage());
        return "error/4xx";
    }

    // UsernameNotFoundException (존재하지 않는 리소스)
    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException e, Model model) {
        log.error("UsernameNotFoundException: {}", e.getMessage());
        model.addAttribute("message", "존재하지 않는 리소스입니다.");
        return "error/404";
    }

    // 나머지
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        log.error("Exception: {}", e.getMessage());
        model.addAttribute("message", "서버 오류가 발생했습니다.");
        return "error/5xx";
    }
}