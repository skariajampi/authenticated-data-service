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

    @Value(value = "${clientId}")
    private String clientId;

    @Value(value = "${userPoolId}")
    private String userPoolId;

    @Value(value = "${clientSecret}")
    private String clientSecret;

    @Bean
    public String region() {
        return this.region;
    }

    @Bean
    public String clientId() {
        return this.clientId;
    }

    @Bean
    public String clientSecret() {
        return this.clientSecret;
    }

}
