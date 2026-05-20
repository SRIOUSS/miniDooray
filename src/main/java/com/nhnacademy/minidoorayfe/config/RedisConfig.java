package com.nhnacademy.minidoorayfe.config;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

@Configuration
public class RedisConfig {

    // api에서 어떻게 들어오는지에 따라서 JsonMapperBuilderCustomizer.class 를 사용해서 설정 추가해줘야 할지도

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, JsonMapper jsonMapper) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        GenericJacksonJsonRedisSerializer serializer = new GenericJacksonJsonRedisSerializer(jsonMapper);

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);

        return redisTemplate;
    }

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer(JsonMapper jsonMapper) {
        return new GenericJacksonJsonRedisSerializer(jsonMapper);
    }

    @Bean
    public JsonMapperBuilderCustomizer jsonMapperBuilderCustomizer() {
        return builder -> {
            BasicPolymorphicTypeValidator.Builder validatorBuilder =
                    BasicPolymorphicTypeValidator.builder()
                            .allowIfSubType("com.nhnacademy.minidoorayfe")
                            .allowIfSubType("java.lang")
                            .allowIfSubType("java.util");

            builder.activateDefaultTyping(validatorBuilder.build());
        };
    }
}
