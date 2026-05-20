package com.nhnacademy.minidoorayfe.config;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.jackson.SecurityJacksonModules;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

@Configuration
public class RedisConfig {

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
                            .allowIfSubType("com.nhnacademy.minidoorayfe.")
                            .allowIfSubType("org.springframework.security.")
                            .allowIfSubType("java.lang")
                            .allowIfSubType("java.util");

            builder.addModules(
                    SecurityJacksonModules.getModules(getClass().getClassLoader(), validatorBuilder)
            );
        };
    }
}
