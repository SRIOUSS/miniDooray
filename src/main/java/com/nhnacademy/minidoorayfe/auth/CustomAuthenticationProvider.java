package com.nhnacademy.minidoorayfe.auth;

import com.nhnacademy.minidoorayfe.api.AccountApiClient;
import com.nhnacademy.minidoorayfe.dto.auth.AccountResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AccountApiClient accountApiClient;
    private final PasswordEncoder passwordEncoder;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String userId = authentication.getName();
        String userPassword = Objects.requireNonNull(authentication.getCredentials()).toString();

        AccountResponseDto accountResponseDto;
        try {
            accountResponseDto = accountApiClient.findByUserId(userId);
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Invalid credentials");
        } catch (Exception e) {
            throw new AuthenticationServiceException("verify service error", e);
        }

        if (!accountResponseDto.getStatus().equals("ACTIVE")) {
            throw new DisabledException("Account is disabled");
        }

        if (!passwordEncoder.matches(userPassword, accountResponseDto.getUserPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return new UsernamePasswordAuthenticationToken(accountResponseDto, null, List.of()); // 비밀번호, 권한은 필요없음, 권한은 애초에 없지 ㅇㅇ
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
