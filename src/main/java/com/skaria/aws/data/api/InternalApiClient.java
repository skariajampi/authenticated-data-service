package com.skaria.aws.data.api;

import com.skaria.aws.data.model.UserAuthorizationRequestPayload;
import com.skaria.aws.security.model.JwtAuthentication;
import com.skaria.aws.security.model.UserAuthorizationResponsePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Component
public class InternalApiClient {

    private RestTemplate restTemplate;
    private String authorizationUrl;

    @Autowired
    public InternalApiClient(String authorizationUrl, RestTemplate restTemplate) {
        this.authorizationUrl = authorizationUrl;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<UserAuthorizationResponsePayload> getAuthorization(String authToken) {

            UserAuthorizationRequestPayload userAuthorizationRequestPayload =
                    new UserAuthorizationRequestPayload();
            userAuthorizationRequestPayload.setAuthToken(authToken);

            ResponseEntity<UserAuthorizationResponsePayload> result =
                    restTemplate.postForEntity(
                            this.authorizationUrl,
                            userAuthorizationRequestPayload,
                            UserAuthorizationResponsePayload.class,
                            new HashMap<>());
            return result;


    }


}
