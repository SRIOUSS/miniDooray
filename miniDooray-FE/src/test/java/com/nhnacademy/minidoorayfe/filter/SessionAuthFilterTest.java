package com.nhnacademy.minidoorayfe.filter;

import com.nhnacademy.minidoorayfe.resolver.SessionConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SessionAuthFilterTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain filterChain;

    @Mock
    HttpSession session;

    @InjectMocks
    SessionAuthFilter sessionAuthFilter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void noSession() throws ServletException, IOException {
        given(request.getSession(false)).willReturn(null);
        sessionAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void existSessionNonExistSessionKey() throws ServletException, IOException {
        given(request.getSession(false)).willReturn(session);
        given(session.getAttribute(SessionConstants.SESSION_KEY)).willReturn(null);

        sessionAuthFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void existSessionExistSessionKey() throws ServletException, IOException {
        given(request.getSession(false)).willReturn(session);
        given(session.getAttribute(SessionConstants.SESSION_KEY)).willReturn("SESSION_ACCOUNT");

        sessionAuthFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()).isTrue();

    }
}