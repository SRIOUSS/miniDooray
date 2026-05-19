package com.nhnacademy.minidoorayauthapi.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class LoginFailureCounter {

    private static final String KEY_PREFIX = "login:fails:%s";
    private static final Long TTL_MINUTES = 1L;

    private final RedisTemplate<String, Object> redisTemplate;

    public void increment(String username) {
        String key = KEY_PREFIX.formatted(username);
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, TTL_MINUTES, TimeUnit.MINUTES); // 실패 횟수 증가 만료 시간 1분
    }

    public int getFailures(String username) {
        Object value = redisTemplate.opsForValue().get(KEY_PREFIX.formatted(username));
        if (Objects.isNull(value)) {
            return 0;
        }
        return Integer.parseInt(String.valueOf(value));
    }

    public void reset(String username) {
        redisTemplate.delete(KEY_PREFIX.formatted(username));
    }
}
