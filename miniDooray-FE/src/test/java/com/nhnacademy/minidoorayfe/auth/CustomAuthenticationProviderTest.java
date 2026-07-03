package com.nhnacademy.minidoorayfe.auth;

import com.nhnacademy.minidoorayfe.api.AccountApiClient;
import com.nhnacademy.minidoorayfe.dto.auth.AccountResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationProviderTest {

    @Mock
    AccountApiClient accountApiClient;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    CustomAuthenticationProvider customAuthenticationProvider;

    @Mock
    Authentication authentication;

    AccountResponseDto accountResponseDto;

    @BeforeEach
    void setUp() {
        accountResponseDto = new AccountResponseDto();
        accountResponseDto.setUserId("testUser");
        accountResponseDto.setUserPassword("encodedPassword");
        accountResponseDto.setStatus("ACTIVE");
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {
        given(authentication.getName()).willReturn("testUser");
        given(authentication.getCredentials()).willReturn("rawPassword");
        given(accountApiClient.findByUserId("testUser")).willReturn(accountResponseDto);
        given(passwordEncoder.matches("rawPassword", "encodedPassword")).willReturn(true);

        Authentication result = customAuthenticationProvider.authenticate(authentication);
        assertThat(result).isNotNull();
        assertThat(result.getPrincipal()).isEqualTo(accountResponseDto);
    }

    @Test
    @DisplayName("존재하지 않는 유저")
    void notExistsUser() {
        given(authentication.getName()).willReturn("testUser");
        given(authentication.getCredentials()).willReturn("rawPassword");
        given(accountApiClient.findByUserId("testUser"))
                .willThrow(new UsernameNotFoundException("testUser"));
        assertThatThrownBy(() -> customAuthenticationProvider.authenticate(authentication))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @DisplayName("휴먼 계정")
    void dormantUser() {
        given(authentication.getName()).willReturn("testUser");
        given(authentication.getCredentials()).willReturn("rawPassword");
        accountResponseDto.setStatus("DORMANT");
        given(accountApiClient.findByUserId("testUser")).willReturn(accountResponseDto);
        assertThatThrownBy(() -> customAuthenticationProvider.authenticate(authentication))
                .isInstanceOf(DisabledException.class);
    }

    @Test
    @DisplayName("비밀번호 불일치")
    void passwordMismatch() {
        given(authentication.getName()).willReturn("testUser");
        given(authentication.getCredentials()).willReturn("rawPassword");
        given(accountApiClient.findByUserId("testUser")).willReturn(accountResponseDto);
        given(passwordEncoder.matches("rawPassword", "encodedPassword")).willReturn(false);
        assertThatThrownBy(() -> customAuthenticationProvider.authenticate(authentication))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @DisplayName("외부 서비스 오류")
    void externalServiceError() {
        given(authentication.getName()).willReturn("testUser");
        given(authentication.getCredentials()).willReturn("rawPassword");
        given(accountApiClient.findByUserId("testUser"))
                .willThrow(new RuntimeException("Server Error"));
        assertThatThrownBy(() -> customAuthenticationProvider.authenticate(authentication))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("support")
    void supports() {
        assertThat(customAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class)).isTrue();
        assertThat(customAuthenticationProvider.supports(Authentication.class)).isFalse();
    }
}