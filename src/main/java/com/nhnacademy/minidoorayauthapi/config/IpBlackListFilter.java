package com.nhnacademy.minidoorayauthapi.config;

import com.nhnacademy.minidoorayauthapi.auth.BlackList;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class IpBlackListFilter extends OncePerRequestFilter {

    private final BlackList blackList;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain

    ) throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        if (blackList.isBlocked(ip)) {
            response.sendRedirect("/login?error=blocked");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
