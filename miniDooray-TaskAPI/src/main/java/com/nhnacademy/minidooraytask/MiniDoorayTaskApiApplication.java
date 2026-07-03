package com.nhnacademy.minidooraytask;

import com.nhnacademy.minidooraytask.config.ApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(ApiProperties.class)
@SpringBootApplication
public class MiniDoorayTaskApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniDoorayTaskApiApplication.class, args);
    }

}
