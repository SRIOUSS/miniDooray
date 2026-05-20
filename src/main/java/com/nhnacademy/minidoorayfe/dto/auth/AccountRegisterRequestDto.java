package com.nhnacademy.minidoorayfe.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountRegisterRequestDto {
    private String userId;
    private String userPassword;
    private String userName;
    private String userEmail;
}
