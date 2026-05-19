package com.nhnacademy.minidoorayfe.dto.auth;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class SessionAccountDto implements Serializable {

    // 로그인 성공 시 '세션에 저장되는 인증 정보'
    // 모든 요청에서 '지금 누가 로그인 했는지'를 나타내는 객체

    // Serializable: Redis에 직렬화해서 저장하기 위해
    // @JsonTypeInfo: Redis에서 역직렬화 할 때 타입 정보 포함

    @Serial
    private static final long serialVersionUID = 1L;

    private Long accountId;
    private String userId;
}