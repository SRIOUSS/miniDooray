package com.nhnacademy.minidoorayfe;

import com.nhnacademy.minidoorayfe.properties.ApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication()
@EnableConfigurationProperties(ApiProperties.class)
public class MiniDoorayFeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniDoorayFeApplication.class, args);
    }

}
