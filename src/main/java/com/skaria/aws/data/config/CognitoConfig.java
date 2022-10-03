package com.skaria.aws.data.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CognitoConfig {

    @Value(value = "${security.region}")
    private String region;

    @Value(value = "${security.access-key}")
    private String accessKey;

    @Value(value = "${security.access-secret}")
    private String secretKey;

    @Value(value = "${userPoolId}")
    private String userPoolId;

    @Bean
    public String region() {
        return this.region;
    }

}
