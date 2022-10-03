package com.skaria.aws.data.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Jwt configuration properties model.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "security")
public class AuthenticationServiceConfig {

    /*private String authenticationService;
    private String authorizationUri;*/
    private String jwkUrl;
    private int connectionTimeout;
    private int readTimeout;
    /*private String userNameField;
    private String groupsField;
    @Value(value = "${spring.security.oauth2.client.provider.cognito.issuerUri}")
    private String cognitoIdentityPoolUrl;*/

    /*public String authorizationUrl(){

        return String.format("%s%s", this.authenticationService, authorizationUri);
    }*/


}
