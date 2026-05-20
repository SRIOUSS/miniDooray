package com.nhnacademy.minidoorayfe.api;

import com.nhnacademy.minidoorayfe.dto.auth.LoginFormDto;
import com.nhnacademy.minidoorayfe.dto.auth.SessionAccountDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AuthApiClient {

    private final RestClient restClient;

    public AuthApiClient(@Qualifier("gatewayRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public SessionAccountDto login(LoginFormDto dto) {
        String url = "/auth-api/login";
        return restClient.post()
                .uri(url)
                .body(dto)
                .retrieve()
                .body(SessionAccountDto.class);
    }
}
