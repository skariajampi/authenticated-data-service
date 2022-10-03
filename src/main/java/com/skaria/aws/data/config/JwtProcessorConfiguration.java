package com.skaria.aws.data.config;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.skaria.aws.data.converter.CognitoAccessTokenConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.net.MalformedURLException;
import java.net.URL;

import static com.nimbusds.jose.JWSAlgorithm.RS256;

/**
 * The {@link ConfigurableJWTProcessor} configuration.
 */
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtProcessorConfiguration {

    private final AuthenticationServiceConfig configuration;

    /**
     * Configure and register jwt processor.
     *
     * @return the configurable jwt processor
     * @throws MalformedURLException the malformed url exception
     */
    @Bean
    public ConfigurableJWTProcessor configurableJWTProcessor() throws MalformedURLException {
        final ResourceRetriever resourceRetriever
                = new DefaultResourceRetriever(configuration.getConnectionTimeout(), configuration.getReadTimeout());

        final ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();

        final URL jwkSetURL = new URL(configuration.getJwkUrl());
        final JWKSource keySource = new RemoteJWKSet(jwkSetURL, resourceRetriever);
        final JWSKeySelector keySelector = new JWSVerificationKeySelector(RS256, keySource);
        jwtProcessor.setJWSKeySelector(keySelector);

        return jwtProcessor;
    }

    private static final String ROLES_CLAIM_NAME = "roles";
    private static final String ROLE_PREFIX = "ROLE_";

    @Bean
    public JwtAuthenticationConverter getJwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
                new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(ROLES_CLAIM_NAME);
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(ROLE_PREFIX);
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    public CognitoAccessTokenConverter cognitoAccessTokenConverter() {
        return new CognitoAccessTokenConverter();
    }
}
