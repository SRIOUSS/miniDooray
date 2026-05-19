package com.nhnacademy.minidoorayauthapi.controller;

import com.nhnacademy.minidoorayauthapi.api.AccountApiClient;
import com.nhnacademy.minidoorayauthapi.dto.AccountRegisterRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AccountApiClient accountApiClient;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/check-userId")
    public Map<String, Boolean> checkUserId(@RequestParam String userId) {
        try {
            accountApiClient.findByUserId(userId);
            return Map.of("exists", true);
        } catch (UsernameNotFoundException e) {
            return Map.of("exists", false);
        }
    }

    // 회원가입 로직
    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody @Valid AccountRegisterRequestDto dto) {
        AccountRegisterRequestDto encodedDto = new AccountRegisterRequestDto(
                dto.getUserId(),
                passwordEncoder.encode(dto.getUserPassword()),
                dto.getUserName(),
                dto.getUserEmail()
        );

        accountApiClient.register(encodedDto);
        return ResponseEntity.ok().build();
    }
}