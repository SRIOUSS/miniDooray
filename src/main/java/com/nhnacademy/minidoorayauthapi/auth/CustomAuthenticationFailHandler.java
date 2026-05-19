package com.nhnacademy.minidoorayauthapi.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFailHandler implements AuthenticationFailureHandler {

    private final LoginFailureCounter loginFailureCounter; // 로그인 실패했을 때 로그인 락에 대해서 email로 보내는거 생각해보기
    private final BlackList blackList;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        @NonNull HttpServletResponse response,
                                        @NonNull AuthenticationException exception
    ) throws IOException {

        String ip = request.getRemoteAddr();

        if (exception instanceof DisabledException) {
            response.sendRedirect("/login?error=disabled");
            return;
        }

        if (exception instanceof LockedException) {
            response.sendRedirect("/login?error=blocked");
            return;
        }

        loginFailureCounter.increment(ip);

        if (loginFailureCounter.getFailures(ip) >= 3) {
            blackList.blockLogin(ip);
            loginFailureCounter.reset(ip);

            log.info("login is blocked, ip: {}", ip);

            response.sendRedirect("/login?error=blocked");

            return;
        }

        log.info("로그인 실패: {}", ip);

        response.sendRedirect("login?error=true");
    }
}
