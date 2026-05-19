package com.nhnacademy.minidooraytask.client.dto.account;

import java.util.List;

public record AccountListReq(
        List<Long> accountIdList
) {
}