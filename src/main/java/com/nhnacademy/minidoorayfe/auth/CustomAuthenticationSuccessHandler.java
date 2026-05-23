package com.nhnacademy.minidoorayfe.auth;

import com.nhnacademy.minidoorayfe.resolver.SessionConstants;
import com.nhnacademy.minidoorayfe.dto.auth.AccountResponseDto;
import com.nhnacademy.minidoorayfe.dto.auth.SessionAccountDto;
import jakarta.servlet.ServletException;
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
                                        @NonNull HttpServletResponse response,
                                        Authentication authentication
    ) throws IOException, ServletException {

        AccountResponseDto accountResponseDto = (AccountResponseDto) authentication.getPrincipal();

        assert accountResponseDto != null;
        SessionAccountDto sessionAccount = new SessionAccountDto(
                accountResponseDto.getAccountId(),
                accountResponseDto.getUserId()
        );

        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConstants.SESSION_KEY, sessionAccount);

        String ip = request.getRemoteAddr();

        loginFailureCounter.reset(ip);

        log.info("Login Success: {}", accountResponseDto.getUserId());

        setDefaultTargetUrl("/projects");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
