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
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.DefaultTyping;
@Configuration
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

//    @Bean
//    public JsonMapperBuilderCustomizer jsonMapperBuilderCustomizer() {
//        return builder -> {
//            BasicPolymorphicTypeValidator.Builder validatorBuilder =
//                    BasicPolymorphicTypeValidator.builder()
//                            .allowIfSubType("com.nhnacademy.minidoorayfe.")
//                            .allowIfSubType("org.springframework.security.")
//                            .allowIfSubType("java.lang")
//                            .allowIfSubType("java.util");
//       builder.activateDefaultTypingAsProperty(
//                validatorBuilder,
//                DefaultTyping.NON_FINAL,
//                "@class"
//        );
//            builder.addModules(
//                    SecurityJacksonModules.getModules(getClass().getClassLoader(), validatorBuilder)
//            );
//        };
//    }

    @Bean
    public ObjectMapper redisObjectMapper() {
        BasicPolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.nhnacademy.minidoorayfe.")
                .allowIfSubType("org.springframework.security.")
                .allowIfSubType("java.lang.")
                .allowIfSubType("java.util.")
                .allowIfSubType("java.time.")
                .build();

        return JsonMapper.builder()
                .activateDefaultTypingAsProperty(ptv, DefaultTyping.NON_CONCRETE_AND_ARRAYS, "@class")
                .addModules(SecurityJacksonModules.getModules(
                        RedisConfig.class.getClassLoader()))
                .build();
    }
}
