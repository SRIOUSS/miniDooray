package com.nhnacademy.minidooraytask.client;

import com.nhnacademy.minidooraytask.client.dto.account.AccountListReq;
import com.nhnacademy.minidooraytask.client.dto.account.AccountListResp;
import com.nhnacademy.minidooraytask.client.dto.account.AccountResp;
import com.nhnacademy.minidooraytask.config.ApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountApiClient {

    private final RestClient restClient;
    private final ApiProperties properties;

    public AccountResp getAccountById(long accountId) {
        return restClient.get()
                .uri("%s/%s".formatted(properties.getAccountUrl(), accountId))
                .retrieve()
                .body(AccountResp.class);
    }

    public Map<Long, AccountResp> getAccountByIds(List<Long> accountIds) {
        List<Long> accountIdList = new ArrayList<>(new HashSet<>(accountIds));

        AccountListResp body = restClient.post()
                .uri(properties.getAccountUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .body(new AccountListReq(accountIdList))
                .retrieve()
                .body(AccountListResp.class);

        return Optional.ofNullable(body)
                .map(b -> b.accountRespList().stream().collect(Collectors.toMap(AccountResp::id, a -> a)))
                .orElse(Collections.emptyMap());
    }

    public AccountResp getAccountByUserId(String userId) {
        return restClient.get()
                .uri("%s?userId=%s".formatted(properties.getAccountUrl(), userId))
                .retrieve()
                .body(AccountResp.class);
    }
}