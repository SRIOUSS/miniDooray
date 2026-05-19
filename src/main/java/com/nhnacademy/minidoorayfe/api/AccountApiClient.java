package com.nhnacademy.minidoorayfe.api;

import com.nhnacademy.minidoorayfe.dto.auth.AccountRegisterRequestDto;
import com.nhnacademy.minidoorayfe.dto.auth.LoginFormDto;
import com.nhnacademy.minidoorayfe.dto.auth.SessionAccountDto;
import com.nhnacademy.minidoorayfe.properties.ApiProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AccountApiClient {

    private final RestClient restClient;
    private final ApiProperties properties;

    public AccountApiClient(@Qualifier("gatewayRestClient") RestClient restClient,
                            ApiProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
    }

    public boolean checkUserId(String userId) {
        String url = "%s/account-api/v1/accounts/check-userId?userId=%s"
                .formatted(properties.getGatewayUrl(), userId);
        return Boolean.TRUE.equals(restClient.get().uri(url).retrieve()
                .body(Boolean.class));
    }


    public void register(AccountRegisterRequestDto accountRegisterRequestDto) {
        String url = "%s/account-api/v1/accounts/register".formatted(properties.getGatewayUrl());
        restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(accountRegisterRequestDto)
                .retrieve()
                .toBodilessEntity(); // body 없이 상태 코드만
    }

    public SessionAccountDto login(LoginFormDto dto) {
        String url = "%s/auth-api/login".formatted(properties.getGatewayUrl());
        return restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .body(SessionAccountDto.class);
    }
}
