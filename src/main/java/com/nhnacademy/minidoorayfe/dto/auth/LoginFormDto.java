package com.nhnacademy.minidoorayfe.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LoginFormDto {
    private String userId;
    private String userPassword;
}