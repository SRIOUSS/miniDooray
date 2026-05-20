package com.nhnacademy.minidoorayfe.config;

import com.nhnacademy.minidoorayfe.auth.CustomAuthenticationFailHandler;
import com.nhnacademy.minidoorayfe.auth.CustomAuthenticationProvider;
import com.nhnacademy.minidoorayfe.auth.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableRedisIndexedHttpSession
public class SecurityConfig {

    private final CustomAuthenticationProvider provider;
    private final CustomAuthenticationSuccessHandler loginSuccessHandler;
    private final CustomAuthenticationFailHandler loginFailHandler;
    private final RedisIndexedSessionRepository sessionRepository;
    private final IpBlackListFilter ipBlackListFilter;

    @Bean
    public SpringSessionBackedSessionRegistry<RedisIndexedSessionRepository.RedisSession> sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher(); // 세션 만료 이벤트 감지
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity.authenticationProvider(provider)
                .addFilterBefore(ipBlackListFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/login", "/signup", "/error").permitAll()
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
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("SESSION")
                        .logoutSuccessUrl("/login")
                )
                // Session Fixation Attack 방지
                .sessionManagement(session -> session.
                        sessionFixation()
                        .newSession()
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .sessionRegistry(sessionRegistry()))

                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny) // Clickjacking 방지
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; style-src 'self'; script-src 'self' 'unsafe-inline';"))
                );
        // XXS(Cross-site Scripting) 방지, 같은 도메인 리소스만 허용, 외부 스크립트, 이미지, 폰트 증 외부 리소스 차단
        // 외부 폰트 사용이나 외부 CDN, 이미지를 사용하면 여기에 추가 필요

        // Thymeleaf `th:action` 이 자동으로 토큰 추가해줌, CSRF REST API로 바꾸면 나중에 비활성화 해줘야할 수 있음
        return httpSecurity.build();
    }
}
