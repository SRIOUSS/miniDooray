package com.nhnacademy.minidoorayfe.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AccountResponseDto {
    private Long accountId;
    private String userId;

    private String userPassword;
    private String status;
}
