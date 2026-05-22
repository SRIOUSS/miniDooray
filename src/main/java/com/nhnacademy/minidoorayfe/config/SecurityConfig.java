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

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationProvider provider;
    private final CustomAuthenticationSuccessHandler loginSuccessHandler;
    private final CustomAuthenticationFailHandler loginFailHandler;
    private final IpBlackListFilter ipBlackListFilter;
    private final SessionAuthFilter sessionAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();

        httpSecurity.authenticationProvider(provider)
                .addFilterBefore(ipBlackListFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(sessionAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/signup", "/error").permitAll()
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
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfTokenRepository)
                        .csrfTokenRequestHandler(requestHandler)
                );
        // Thymeleaf `th:action` 이 자동으로 토큰 추가해줌, CSRF REST API로 바꾸면 나중에 비활성화 해줘야할 수 있음
        return httpSecurity.build();
    }
}
