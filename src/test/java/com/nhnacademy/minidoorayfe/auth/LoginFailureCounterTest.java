package com.nhnacademy.minidoorayfe.auth;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@ExtendWith(MockitoExtension.class)
class LoginFailureCounterTest {

    @Mock
    RedisTemplate<String, Object> redisTemplate;

    @Mock
    ValueOperations<String, Object> valueOperations;

    @InjectMocks
    LoginFailureCounter loginFailureCounter;

    @Test
    void increment() {
        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        loginFailureCounter.increment("127.0.0.1");

        verify(valueOperations).increment("login:fails:127.0.0.1");
        verify(redisTemplate).expire("login:fails:127.0.0.1", 1L, TimeUnit.MINUTES);
    }

    @Test
    void getFailures1() {
        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        given(valueOperations.get("login:fails:127.0.0.1")).willReturn("2");

        int result = loginFailureCounter.getFailures("127.0.0.1");

        assertThat(result).isEqualTo(2);
    }

    @Test
    void getFailures2() {
        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        given(valueOperations.get("login:fails:127.0.0.1")).willReturn(null);

        int result = loginFailureCounter.getFailures("127.0.0.1");

        assertThat(result).isZero();
    }

    @Test
    void reset() {
        loginFailureCounter.reset("127.0.0.1");
        verify(redisTemplate).delete("login:fails:127.0.0.1");
    }


}