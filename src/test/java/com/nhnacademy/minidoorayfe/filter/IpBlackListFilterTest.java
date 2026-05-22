package com.nhnacademy.minidoorayfe.filter;

import com.nhnacademy.minidoorayfe.auth.BlackList;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IpBlackListFilterTest {

    @Mock
    BlackList blackList;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain filterChain;

    @InjectMocks
    IpBlackListFilter ipBlackListFilter;

    @BeforeEach
    void setUp() {
        given(request.getRemoteAddr()).willReturn("127.0.0.1");
    }

    @Test
    void doFilterInternal1() throws ServletException, IOException {
        given(blackList.isBlocked("127.0.0.1")).willReturn(true);
        ipBlackListFilter.doFilterInternal(request, response, filterChain);

        verify(response).sendRedirect("/login?error=blocked");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal2() throws ServletException, IOException {
        given(blackList.isBlocked("127.0.0.1")).willReturn(false);

        ipBlackListFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(response, never()).sendRedirect(any());
    }
}