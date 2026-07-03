package com.nhnacademy.minidoorayfe.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationFailHandlerTest {

    @Mock LoginFailureCounter loginFailureCounter;
    @Mock BlackList blackList;
    @InjectMocks CustomAuthenticationFailHandler customAuthenticationFailHandler;
    @Mock HttpServletRequest request;
    @Mock HttpServletResponse response;

    @BeforeEach
    void setUp() {
        given(request.getRemoteAddr()).willReturn("127.0.0.1");
    }

    @Test
    @DisplayName("DORMANT user")
    void dormantUser() throws IOException {
        customAuthenticationFailHandler.onAuthenticationFailure(request, response, new DisabledException("disable"));

        verify(response).sendRedirect("/login?error=disabled");
        verifyNoInteractions(loginFailureCounter, blackList); // 이 Mock들과 한번도 상호작용이 없었는가 검증 ㅇㅇ
    }

    @Test
    @DisplayName("LockException")
    void lockUser() throws IOException {
        customAuthenticationFailHandler.onAuthenticationFailure(request, response, new LockedException("locked"));

        verify(response).sendRedirect("/login?error=blocked");
        verifyNoInteractions(loginFailureCounter, blackList);
    }

    @Test
    @DisplayName("fail <= 3")
    void normalFail() throws IOException {
        given(loginFailureCounter.getFailures("127.0.0.1")).willReturn(2);

        customAuthenticationFailHandler.onAuthenticationFailure(request, response, new BadCredentialsException("bad"));

        verify(loginFailureCounter).increment("127.0.0.1");
        verify(response).sendRedirect("login?error=true");
        verifyNoInteractions(blackList);
    }

    @Test
    @DisplayName("fail >= 3")
    void manyFail() throws IOException {
        given(loginFailureCounter.getFailures("127.0.0.1")).willReturn(3);

        customAuthenticationFailHandler.onAuthenticationFailure(request, response, new BadCredentialsException("bad"));

        verify(loginFailureCounter).increment("127.0.0.1");
        verify(blackList).blockLogin("127.0.0.1");
        verify(loginFailureCounter).reset("127.0.0.1");
        verify(response).sendRedirect("/login?error=blocked");
    }

}