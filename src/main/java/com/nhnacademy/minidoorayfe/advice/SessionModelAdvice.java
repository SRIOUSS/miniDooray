package com.nhnacademy.minidoorayfe.advice;

import com.nhnacademy.minidooraygateway.dto.auth.SessionAccountDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import tools.jackson.databind.json.JsonMapper;

import java.util.LinkedHashMap;

@ControllerAdvice
@RequiredArgsConstructor
public class SessionModelAdvice {

    private final JsonMapper jsonMapper;

    @ModelAttribute("sessionAccount")
    public SessionAccountDto getSessionAccount(HttpSession session) {

        Object object = session.getAttribute(SessionConstants.SESSION_KEY); // SESSION_ACCOUNT 로 꺼냄

        // LinkedHashMap으로 나올 수 있으니 JsonMapper로 변환
        // LinkedHashMap 변환이 필요한 이유: Redis에서 꺼낼 때 SessionAccountDto로 바로 역직렬화가 안 되고 LinkedHashMap으로 나오는 경우 있으므로..
        if (object instanceof LinkedHashMap<?, ?> map) {
            return jsonMapper.convertValue(map, SessionAccountDto.class);
        }

        // @ModelAttribute("sessionAccount")로 Model에 담음
        return (SessionAccountDto) object;
    }
}