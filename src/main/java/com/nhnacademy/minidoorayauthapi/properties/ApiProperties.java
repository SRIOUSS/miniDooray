package com.nhnacademy.minidoorayauthapi.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "api")
public class ApiProperties {
    private String accountUrl;
    private String taskUrl;
}
