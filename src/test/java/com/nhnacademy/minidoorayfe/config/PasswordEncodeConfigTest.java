package com.nhnacademy.minidoorayfe.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class PasswordEncodeConfigTest {

    PasswordEncodeConfig passwordEncodeConfig = new PasswordEncodeConfig();

    @Test
    void passwordEncode() {
        PasswordEncoder passwordEncoder = passwordEncodeConfig.passwordEncoder();

        assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder.class);
    }

    @Test
    void passwordEncoderMatching() {
        PasswordEncoder passwordEncoder = passwordEncodeConfig.passwordEncoder();
        String rawPassword = "testPassword123";

        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
        assertThat(passwordEncoder.matches("wrongPassword", encodedPassword)).isFalse();
    }
}