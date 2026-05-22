package com.nhnacademy.minidooraytask.client.dto.account;

import java.time.LocalDateTime;

public record AccountResp(
        long id,
        String userId,
        String email,
        String Name,
        UserStatus status,
        LocalDateTime createAt
) {
}
