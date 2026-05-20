package com.nhnacademy.minidoorayfe.advice;

import com.nhnacademy.minidoorayfe.dto.auth.SessionAccountDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import tools.jackson.databind.json.JsonMapper;

import java.util.LinkedHashMap;

// TODO 기존 SessionModelAdvice에서 발전시킨(?)
@RequiredArgsConstructor
public class SessionArgumentResolver implements HandlerMethodArgumentResolver {

    private final JsonMapper jsonMapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SessionIdentity.class);
    }

    @Override
    public @Nullable Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = req.getSession();

        Object object = session.getAttribute(SessionConstants.SESSION_KEY); // SESSION_ACCOUNT 로 꺼냄

        // LinkedHashMap으로 나올 수 있으니 JsonMapper로 변환
        // LinkedHashMap 변환이 필요한 이유: Redis에서 꺼낼 때 SessionAccountDto로 바로 역직렬화가 안 되고 LinkedHashMap으로 나오는 경우 있으므로..
        if (object instanceof LinkedHashMap<?, ?> map) {
            return jsonMapper.convertValue(map, SessionAccountDto.class);
        }

        // @ModelAttribute("sessionAccount")로 Model에 담음
        return object;
    }
}
