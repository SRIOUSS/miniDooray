package com.nhnacademy.minidoorayauthapi.api;

import com.nhnacademy.minidoorayauthapi.dto.AccountRegisterRequestDto;
import com.nhnacademy.minidoorayauthapi.dto.AccountResponseDto;
import com.nhnacademy.minidoorayauthapi.properties.ApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class AccountApiClient {

    private final RestClient restClient;
    private final ApiProperties properties;

    public AccountResponseDto findByUserId(String userId) {
        String url = "%s/accounts/login?userId=%s".formatted(properties.getAccountUrl(), userId);
        return restClient.get().uri(url).retrieve()
                .body(AccountResponseDto.class); // 응답 body를 AccountResponseDto로 역직렬화해서 반환
    }

    public void register(AccountRegisterRequestDto accountRegisterRequestDto) {
        String url = "%s/accounts/register".formatted(properties.getAccountUrl());
        restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(accountRegisterRequestDto)
                .retrieve()
                .toBodilessEntity(); // body 없이 상태 코드만
    }
}