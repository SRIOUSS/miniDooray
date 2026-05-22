package com.nhnacademy.minidoorayfe.auth;

import com.nhnacademy.minidoorayfe.dto.auth.AccountResponseDto;
import com.nhnacademy.minidoorayfe.dto.auth.SessionAccountDto;
import com.nhnacademy.minidoorayfe.resolver.SessionConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationSuccessHandlerTest {

    @InjectMocks CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Mock LoginFailureCounter loginFailureCounter;
    @Mock HttpServletRequest request;
    @Mock HttpServletResponse response;
    @Mock Authentication authentication;
    @Mock HttpSession oldSession;
    @Mock HttpSession newSession;

    @Test
    @DisplayName("기존 세션이 없는 경우의 로그인 성공")
    void newSessionLoginSuccess() throws ServletException, IOException {
        AccountResponseDto accountResponseDto = new AccountResponseDto();
        accountResponseDto.setAccountId(1L);
        accountResponseDto.setUserId("testUserId");

        given(authentication.getPrincipal()).willReturn(accountResponseDto);
        given(request.getSession(false)).willReturn(null);
        given(request.getSession()).willReturn(newSession);
        given(request.getRemoteAddr()).willReturn("127.0.0.1");
        given(request.getContextPath()).willReturn("");

        customAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        verify(oldSession, never()).invalidate();
        verify(newSession).setAttribute(eq(SessionConstants.SESSION_KEY), any(SessionAccountDto.class));
        verify(loginFailureCounter).reset("127.0.0.1");
    }

    @Test
    @DisplayName("기존 세션이 있는 경우의 로그인 성공")
    void oldSessionLoginSuccess() throws ServletException, IOException {
        AccountResponseDto accountResponseDto = new AccountResponseDto();
        accountResponseDto.setAccountId(1L);
        accountResponseDto.setUserId("testUser");

        given(authentication.getPrincipal()).willReturn(accountResponseDto);
        given(request.getSession(false)).willReturn(oldSession);
        given(request.getSession()).willReturn(newSession);
        given(request.getRemoteAddr()).willReturn("127.0.0.1");
        given(request.getContextPath()).willReturn("");

        customAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        verify(oldSession).invalidate();
        verify(newSession).setAttribute(eq(SessionConstants.SESSION_KEY), any(SessionAccountDto.class));
        verify(loginFailureCounter).reset("127.0.0.1");
    }




}