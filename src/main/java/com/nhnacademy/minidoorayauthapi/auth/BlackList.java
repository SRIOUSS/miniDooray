package com.nhnacademy.minidoorayauthapi.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class BlackList {

    private static final String KEY_PREFIX = "login:block:%s";
    private static final Long BLOCKING_TIME = 1L;

    private final RedisTemplate<String, Object> redisTemplate;

    public void blockLogin(String userId) {
        String key = KEY_PREFIX.formatted(userId);
        redisTemplate.opsForValue().set(key, "blocked", BLOCKING_TIME, TimeUnit.MINUTES);
    }

    public boolean isBlocked(String userId) {
        String key = KEY_PREFIX.formatted(userId);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
