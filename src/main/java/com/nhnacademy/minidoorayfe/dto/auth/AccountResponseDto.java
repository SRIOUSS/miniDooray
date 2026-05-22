package com.nhnacademy.minidoorayfe.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class AccountResponseDto {
    private Long accountId;
    private String userId;

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userPassword;
    private String status;
}
