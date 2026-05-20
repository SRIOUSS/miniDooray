package com.nhnacademy.minidoorayfe.controller.auth;

import com.nhnacademy.minidoorayfe.advice.SessionConstants;
import com.nhnacademy.minidoorayfe.api.AccountApiClient;
import com.nhnacademy.minidoorayfe.api.AuthApiClient;
import com.nhnacademy.minidoorayfe.dto.auth.LoginFormDto;
import com.nhnacademy.minidoorayfe.dto.auth.SessionAccountDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthApiClient authApiClient;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute LoginFormDto dto, HttpSession session) {
        SessionAccountDto account = this.authApiClient.login(dto);
        session.setAttribute(SessionConstants.SESSION_KEY, account);
        return "redirect:/projects";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
