package com.nhnacademy.minidoorayfe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.jackson.SecurityJacksonModules;
import org.springframework.session.config.SessionRepositoryCustomizer;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

@Configuration
@EnableRedisIndexedHttpSession
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, ObjectMapper redisObjectMapper) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        GenericJacksonJsonRedisSerializer serializer = new GenericJacksonJsonRedisSerializer(redisObjectMapper);

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);

        return redisTemplate;
    }

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer(ObjectMapper redisObjectMapper) {
        return new GenericJacksonJsonRedisSerializer(redisObjectMapper);
    }

    @Bean
    public ObjectMapper redisObjectMapper() {
        BasicPolymorphicTypeValidator.Builder validatorBuilder =
                BasicPolymorphicTypeValidator.builder()
                        .allowIfSubType("com.nhnacademy.minidoorayfe.")
                        .allowIfSubType("org.springframework.security.")
                        .allowIfSubType("org.springframework.")
                        .allowIfSubType("java.lang.")
                        .allowIfSubType("java.util.")
                        .allowIfSubType("java.time.")
                        .allowIfSubType("[");

        return JsonMapper.builder()
                .addModules(SecurityJacksonModules.getModules(
                        RedisConfig.class.getClassLoader(),
                        validatorBuilder))
                .build();

    }

    @Bean
    public SessionRepositoryCustomizer<RedisIndexedSessionRepository> sessionRepositoryCustomizer(
            RedisSerializer<Object> springSessionDefaultRedisSerializer) {
        return repository -> {
            System.out.println("=== Serializer 주입 확인: " + springSessionDefaultRedisSerializer.getClass().getName());

            repository.setDefaultSerializer(springSessionDefaultRedisSerializer);
        };
    }
}
