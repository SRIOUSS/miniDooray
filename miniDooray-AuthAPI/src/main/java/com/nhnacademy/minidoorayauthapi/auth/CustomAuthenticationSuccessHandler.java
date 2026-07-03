package com.nhnacademy.minidoorayauthapi.auth;

import com.nhnacademy.minidoorayauthapi.advice.SessionConstants;
import com.nhnacademy.minidoorayauthapi.dto.AccountResponseDto;
import com.nhnacademy.minidoorayauthapi.dto.SessionAccountDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final LoginFailureCounter loginFailureCounter;

    @Override
    public void onAuthenticationSuccess(@NonNull HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication
    ) throws IOException {

        AccountResponseDto accountResponseDto = (AccountResponseDto) authentication.getPrincipal();

        assert accountResponseDto != null;
        SessionAccountDto sessionAccount = new SessionAccountDto(
                accountResponseDto.getAccountId(),
                accountResponseDto.getUserId()
        );

        HttpSession session = request.getSession();
        session.setAttribute(SessionConstants.SESSION_KEY, sessionAccount);

        String ip = request.getRemoteAddr();

        this.loginFailureCounter.reset(ip);

        log.info("Login Success: {}", accountResponseDto.getUserId());

        response.sendRedirect("/projects");

    }
}
