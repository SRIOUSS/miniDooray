package com.nhnacademy.minidoorayfe.controller.auth;

import com.nhnacademy.minidoorayfe.api.AccountApiClient;
import lombok.RequiredArgsConstructor;
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
        boolean exists = accountApiClient.checkUserId(userId);
        return Map.of("exists", exists);
    }
}
