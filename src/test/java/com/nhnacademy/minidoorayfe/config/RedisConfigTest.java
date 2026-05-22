package com.nhnacademy.minidoorayfe.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.config.SessionRepositoryCustomizer;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RedisConfigTest {

    @Mock
    RedisConnectionFactory redisConnectionFactory;

    RedisConfig redisConfig = new RedisConfig();

    @Test
    void redisObjectMapper() {
        ObjectMapper objectMapper = redisConfig.redisObjectMapper();

        assertThat(objectMapper).isNotNull();
    }

    @Test
    void redisTemplateBean() {
        ObjectMapper objectMapper = redisConfig.redisObjectMapper();
        RedisTemplate<String, Object> redisTemplate = redisConfig.redisTemplate(redisConnectionFactory, objectMapper);

        assertThat(redisTemplate).isNotNull();
        assertThat(redisTemplate.getKeySerializer()).isInstanceOf(StringRedisSerializer.class);
        assertThat(redisTemplate.getHashKeySerializer()).isInstanceOf(StringRedisSerializer.class);
        assertThat(redisTemplate.getValueSerializer()).isInstanceOf(GenericJacksonJsonRedisSerializer.class);
        assertThat(redisTemplate.getHashValueSerializer()).isInstanceOf(GenericJacksonJsonRedisSerializer.class);
    }

    @Test
    void sessionRepositoryBean() {
        ObjectMapper objectMapper = redisConfig.redisObjectMapper();
        RedisSerializer<Object> serializer = redisConfig.springSessionDefaultRedisSerializer(objectMapper);
        SessionRepositoryCustomizer<RedisIndexedSessionRepository> customizer = redisConfig.sessionRepositoryCustomizer(serializer);

        assertThat(customizer).isNotNull();
    }
}