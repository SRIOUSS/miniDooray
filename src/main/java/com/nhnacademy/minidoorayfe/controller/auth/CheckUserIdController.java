package com.nhnacademy.minidoorayfe.controller.auth;

import com.nhnacademy.minidoorayfe.api.AccountApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CheckUserIdController {

    private final AccountApiClient accountApiClient;

    @GetMapping("/check-userId")
    public Map<String, Boolean> checkUserId(@RequestParam String userId) {
        try {
            accountApiClient.findByUserId(userId);
            return Map.of("exists", true);
        } catch (UsernameNotFoundException e) {
            return Map.of("exists", false);
        }
    }
}
