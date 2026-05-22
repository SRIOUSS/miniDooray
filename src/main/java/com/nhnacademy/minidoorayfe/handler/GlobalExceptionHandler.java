package com.nhnacademy.minidoorayfe.handler;

import com.nhnacademy.minidoorayfe.exception.ApiServerException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 404 - 리소스 없음
    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException e, Model model,
                                                  HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        log.error("UsernameNotFoundException: {}", e.getMessage());
        model.addAttribute("statusCode", 404);
        model.addAttribute("message", "요청하신 리소스를 찾을 수 없습니다.");
        return "error/404";
    }

    // 5xx - 서버 오류
    @ExceptionHandler(ApiServerException.class)
    public String handleApiServerException(ApiServerException e, Model model,
                                           HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        log.error("ApiServerException: {}", e.getMessage());
        model.addAttribute("statusCode", 500);
        model.addAttribute("message", "서버에서 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        return "error/5xx";
    }

    // 4xx - API 호출 실패 (400, 403, 409 등)
    @ExceptionHandler(RestClientException.class)
    public String handleRestClientException(RestClientException e, Model model,
                                            HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error("RestClientException: {}", e.getMessage());
        model.addAttribute("statusCode", 400);
        model.addAttribute("message", e.getMessage());
        return "error/4xx";
    }

    // 나머지 예외
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        log.error("Exception: {}", e.getMessage(), e);
        model.addAttribute("statusCode", 500);
        model.addAttribute("message", "서버 오류가 발생했습니다.");
        return "error/5xx";
    }
}