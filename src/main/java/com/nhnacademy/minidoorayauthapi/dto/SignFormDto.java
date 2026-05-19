package com.nhnacademy.minidoorayauthapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignFormDto {
    private String userId;
    private String userPassword;
    private String userName;
    private String userEmail;
}
