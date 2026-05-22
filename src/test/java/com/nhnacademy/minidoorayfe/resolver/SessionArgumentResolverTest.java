package com.nhnacademy.minidoorayfe.resolver;

import com.nhnacademy.minidoorayfe.dto.auth.SessionAccountDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import tools.jackson.databind.json.JsonMapper;

import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SessionArgumentResolverTest {

    @Mock JsonMapper jsonMapper;
    @Mock MethodParameter methodParameter;
    @Mock NativeWebRequest nativeWebRequest;
    @Mock HttpServletRequest request;
    @Mock HttpSession session;

    @InjectMocks SessionArgumentResolver sessionArgumentResolver;

    @Test
    void supportParameter() {
        given(methodParameter.hasParameterAnnotation(SessionIdentity.class)).willReturn(true);
        assertThat(sessionArgumentResolver.supportsParameter(methodParameter)).isTrue();
    }

    @Test
    void supportNonParameter() {
        given(methodParameter.hasParameterAnnotation(SessionIdentity.class)).willReturn(false);
        assertThat(sessionArgumentResolver.supportsParameter(methodParameter)).isFalse();
    }

    @Test
    void resolveTestWhenSessionIsInDto() throws Exception {
        given(nativeWebRequest.getNativeRequest()).willReturn(request);
        given(request.getSession()).willReturn(session);
        SessionAccountDto sessionAccountDto = new SessionAccountDto(1L, "testUser");

        given(session.getAttribute(SessionConstants.SESSION_KEY)).willReturn(sessionAccountDto);

        Object result = sessionArgumentResolver.resolveArgument(methodParameter, null, nativeWebRequest, null);
        assertThat(result).isEqualTo(sessionAccountDto);
    }

    @Test
    void resolveTestWhenSessionIsInLickedHashMap() throws Exception {
        given(nativeWebRequest.getNativeRequest()).willReturn(request);
        given(request.getSession()).willReturn(session);

        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("accountId", 1L);
        map.put("userId", "testUser");

        SessionAccountDto sessionAccountDto = new SessionAccountDto(1L, "testUser");
        given(session.getAttribute(SessionConstants.SESSION_KEY)).willReturn(map);
        given(jsonMapper.convertValue(map, SessionAccountDto.class)).willReturn(sessionAccountDto);

        Object result = sessionArgumentResolver.resolveArgument(methodParameter, null, nativeWebRequest, null);

        assertThat(result).isEqualTo(sessionAccountDto);
        verify(jsonMapper).convertValue(map, SessionAccountDto.class);
    }

    @Test
    void nonSessionIsReturnNull() throws Exception {
        given(nativeWebRequest.getNativeRequest()).willReturn(request);
        given(request.getSession()).willReturn(session);
        given(session.getAttribute(SessionConstants.SESSION_KEY)).willReturn(null);

        Object result = sessionArgumentResolver.resolveArgument(methodParameter, null, nativeWebRequest, null);
        assertThat(result).isNull();
    }
}