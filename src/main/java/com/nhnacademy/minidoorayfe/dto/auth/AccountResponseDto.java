package com.nhnacademy.minidoorayfe.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AccountResponseDto {
    private Long accountId;
    private String userId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userPassword;
    private String status;
}
