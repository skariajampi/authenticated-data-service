package com.skaria.aws.data.config;


import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.skaria.aws.data.api.InternalApiClient;
import com.skaria.aws.data.converter.CognitoAccessTokenConverter;
import com.skaria.aws.data.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

/**
 * Security configuration.
 */
@EnableWebSecurity
@AllArgsConstructor(onConstructor = @__(@Autowired))
@EnableConfigurationProperties({AuthenticationServiceConfig.class})
@EnableGlobalMethodSecurity(prePostEnabled = true,jsr250Enabled = true, securedEnabled = true)
public class TheSecurityConfig extends WebSecurityConfigurerAdapter {

    /*private final AuthenticationServiceConfig authenticationServiceConfig;
    private ConfigurableJWTProcessor configurableJWTProcessor;*/
    private CognitoAccessTokenConverter cognitoAccessTokenConverter;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2ResourceServer()
                    .jwt()
                        .jwtAuthenticationConverter(cognitoAccessTokenConverter);
    }

    /*@Bean
    Supplier<InternalApiClient> internalApiClientSupplier() {
        return () -> {
            InternalApiClient internalApiClient =
                    new InternalApiClient(authorizationUrl(), restTemplate());
            return internalApiClient;
        };
    }

    @Bean
    public String authorizationUrl(){
        return authenticationServiceConfig.authorizationUrl();
    }

    @Bean
    public String userNameField(){
        return authenticationServiceConfig.getUserNameField();
    }

    @Bean
    public String groupsField(){
        return authenticationServiceConfig.getGroupsField();
    }

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil(userNameField(), groupsField(), configurableJWTProcessor);
    }
*/

    @Bean//web client async/sync
    public RestTemplate restTemplate() {
        // Do any additional configuration here
        return new RestTemplate();
    }
}
