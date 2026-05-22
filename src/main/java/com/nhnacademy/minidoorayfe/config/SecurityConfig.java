package com.nhnacademy.minidoorayfe.config;

import com.nhnacademy.minidoorayfe.auth.CustomAuthenticationFailHandler;
import com.nhnacademy.minidoorayfe.auth.CustomAuthenticationProvider;
import com.nhnacademy.minidoorayfe.auth.CustomAuthenticationSuccessHandler;
import com.nhnacademy.minidoorayfe.filter.IpBlackListFilter;
import com.nhnacademy.minidoorayfe.filter.SessionAuthFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AndRequestMatcher;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationProvider provider;
    private final CustomAuthenticationSuccessHandler loginSuccessHandler;
    private final CustomAuthenticationFailHandler loginFailHandler;
    private final IpBlackListFilter ipBlackListFilter;
    private final SessionAuthFilter sessionAuthFilter;

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher(); // 세션 만료 이벤트 감지
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();

        httpSecurity.authenticationProvider(provider)
                .addFilterBefore(ipBlackListFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(sessionAuthFilter, UsernamePasswordAuthenticationFilter.class) // TODO 매 요청마다 세션에 SESSION_ACCOUNT 있으면 시큐리티에게 이 사람 인증됐다고 알려주는 역할
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/signup", "/error").permitAll() // TODO projects 퍼밋 올 제거함
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("userId")
                        .passwordParameter("userPassword")
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout").permitAll()
                        .invalidateHttpSession(true)
                        .deleteCookies("SESSION")
                        .logoutSuccessUrl("/login")
                )
                // Session Fixation Attack 방지
//                .sessionManagement(session -> session.
//                        sessionFixation()
//                        .newSession()
//                        .maximumSessions(1)
//                        .maxSessionsPreventsLogin(false)
//                        .sessionRegistry(sessionRegistry()))

//                .headers(headers -> headers
//                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny) // Clickjacking 방지
//                        .contentSecurityPolicy(csp -> csp
//                                .policyDirectives("default-src 'self'; style-src 'self'; script-src 'self' 'unsafe-inline';"))
//                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfTokenRepository)
                        .csrfTokenRequestHandler(requestHandler)
                );
        // XXS(Cross-site Scripting) 방지, 같은 도메인 리소스만 허용, 외부 스크립트, 이미지, 폰트 증 외부 리소스 차단
        // 외부 폰트 사용이나 외부 CDN, 이미지를 사용하면 여기에 추가 필요

        // Thymeleaf `th:action` 이 자동으로 토큰 추가해줌, CSRF REST API로 바꾸면 나중에 비활성화 해줘야할 수 있음
        return httpSecurity.build();
    }
}
