package com.nhnacademy.minidoorayfe.controller.auth;

import com.nhnacademy.minidoorayfe.api.AccountApiClient;
import com.nhnacademy.minidoorayfe.dto.auth.AccountRegisterRequestDto;
import com.nhnacademy.minidoorayfe.dto.auth.SignFormDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SignUpController {

    private final AccountApiClient accountApiClient;

    @GetMapping("/signup")
    public String signUpPage(Model model) {
        model.addAttribute("signUpFormDto", new SignFormDto());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute SignFormDto signFormDto,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        AccountRegisterRequestDto encodedDto = new AccountRegisterRequestDto(
                signFormDto.getUserId(),
                signFormDto.getUserPassword(),
                signFormDto.getUserName(),
                signFormDto.getUserEmail()
        );

        accountApiClient.register(encodedDto);
        return "redirect:/login";
    }
}
