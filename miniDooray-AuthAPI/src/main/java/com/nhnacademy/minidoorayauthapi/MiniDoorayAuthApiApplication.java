package com.nhnacademy.minidoorayauthapi;

import com.nhnacademy.minidoorayauthapi.properties.ApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApiProperties.class)
public class MiniDoorayAuthApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniDoorayAuthApiApplication.class, args);
    }

}
