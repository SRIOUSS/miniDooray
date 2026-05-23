package com.nhnacademy.minidoorayfe.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BlackListTest {

    @Mock
    RedisTemplate<String, Object> redisTemplate;

    @Mock
    ValueOperations<String, Object> valueOperations;

    @InjectMocks
    BlackList blackList;

    @Test
    @DisplayName("저장 성공")
    void blockLogin() {
        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        String userId = "testUser";
        blackList.blockLogin(userId);
        verify(valueOperations).set("login:block:testUser", "blocked", 1L, TimeUnit.MINUTES);
    }

    @Test
    @DisplayName("차단된 경우")
    void isBlocked() {
        given(redisTemplate.hasKey("login:block:testUser")).willReturn(true);

        boolean result = blackList.isBlocked("testUser");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("차단 안된 경우")
    void isBlocked2() {
        given(redisTemplate.hasKey("login:block:testUser")).willReturn(false);

        boolean result = blackList.isBlocked("testUser");
        assertThat(result).isFalse();
    }
}