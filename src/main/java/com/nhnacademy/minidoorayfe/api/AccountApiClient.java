package com.nhnacademy.minidoorayfe.api;

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


    public void register(SignFormDto signFormDto) {
        String url = "/account-api/v1/accounts/register";
        restClient.post()
                .uri(url)
                .body(signFormDto)
                .retrieve()
                .toBodilessEntity(); // body 없이 상태 코드만
    }
}
