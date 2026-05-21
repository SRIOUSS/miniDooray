package com.nhnacademy.minidoorayfe.filter;

import com.nhnacademy.minidoorayfe.resolver.SessionConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * <만든 이유>
 *
 * 우리 구조: SESSION_ACCOUNT 키로 직접 세션 관리하고 있음
 * 그래서 매 요청마다 세션에 SESSION_ACCOUNT 가 있으면 시큐리티에게 이 사람 인증됐다고 알려주는 브릿지 역할을 하는 필터임
 * SecurityConfig에 NullSecurityContextRepository 설정만 안 하면 됨
 * SessionAuthFilter가 SecurityContextHolder에 인증 정보를 넣어도 NullSecurityContextRepository가 있으면 다음 필터에서 날려버리므로
 *
 * <흐름>
 * 로그인 시 -> SESSION_ACCOUNT 세션에 저장 -> 다음 요청부터 이 필터가 세션 까서 확인 -> 시큐리티에게 인증 정보 주입 -> 시큐리티가 막고 있는 페이지 접근 가능!
 */

// TODO 매번 permitAll()로 다 열어줄 수도 없고, 안 그러면 또 다 로그인으로 튕기기 때문에 추가
@Component
@RequiredArgsConstructor
public class SessionAuthFilter extends OncePerRequestFilter {

    // 매 HTTP 요청마다 이 필터 실행됨
    // 세션에 SESSION_ACCOUNT 가 있으면 시큐리티에게 이 사람 로그인 됐다고 알려주는 놈임

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 현재 요청의 세션 가져옴 (false니까 세션 없으면 새로 안 만들고 널 리턴함)
        HttpSession session = request.getSession(false);

        // 세션이 존재하고, 세션 안에 SESSION_ACCOUNT 키로 저장된 값(로그인 시 저장한 SessionAccountDto) 있으면 -> 로그인 한 사용자라면
        if(Objects.nonNull(session) && Objects.nonNull(session.getAttribute(SessionConstants.SESSION_KEY))) {

            // 시큐리티용 인증 객체 만듦
            // authenticated() 는 이미 인증 완료된 토큰을 생성함
            // "user"는 사용자 식별자, null은 비밀번호(이미 인증됐으니 필요없음), List.of()는 권한 목록임
            UsernamePasswordAuthenticationToken auth = UsernamePasswordAuthenticationToken.authenticated("user", null, List.of());

            // 시큐리티의 인증 컨텍스트에 위에서 만든 인증 객체를 넣음
            // 넣어줘야 시큐리티가 인증된 사용자로 인식해서 SecurityConfig의 anyRequest().authenticated() 를 통과함
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // 다음 필터로 요청 넘김
        // 이거 없으면 요청이 거기에서 멈춰버림
        filterChain.doFilter(request, response);
    }
}