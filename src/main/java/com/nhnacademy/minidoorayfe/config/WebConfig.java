package com.nhnacademy.minidoorayfe.config;

import com.nhnacademy.minidoorayfe.resolver.SessionArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JsonMapper jsonMapper;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        // SessionArgumentResolver를 WebMvcConfigurer에 등록
        // 등록해야 @SessionIdentity 어노테이션 동작함
        resolvers.add(new SessionArgumentResolver(this.jsonMapper));
    }
}
