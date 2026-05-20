package com.nhnacademy.minidoorayfe.api;

import com.nhnacademy.minidoorayfe.dto.auth.AccountResponseDto;
import com.nhnacademy.minidoorayfe.dto.auth.SignFormDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AccountApiClient {

    private final RestClient restClient;

    public AccountApiClient(@Qualifier("gatewayRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public boolean checkUserId(String userId) {
        String url = "/account-api/v1/accounts/check-userId?userId=%s"
                .formatted(userId);
        return Boolean.TRUE.equals(restClient.get().uri(url).retrieve()
                .body(Boolean.class));
    }

    public AccountResponseDto findByUserId(String userId) {
        String url = "/accounts/login?userId=%s".formatted(userId);
        return restClient.get().uri(url).retrieve()
                .body(AccountResponseDto.class); // 응답 body를 AccountResponseDto로 역직렬화해서 반환
    }

    public void register(SignFormDto signFormDto) {
        String url = "/account-api/v1/accounts/register";
        restClient.post()
                .uri(url)
                .body(signFormDto)
                .retrieve()
                .toBodilessEntity(); // body 없이 상태 코드만
    }
}
