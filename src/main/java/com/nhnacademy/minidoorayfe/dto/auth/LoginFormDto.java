package com.nhnacademy.minidoorayfe.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginFormDto {
    private String userId;
    private String userPassword;
}