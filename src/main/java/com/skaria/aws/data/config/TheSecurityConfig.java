package com.skaria.aws.data.config;


import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.skaria.aws.data.api.InternalApiClient;
import com.skaria.aws.data.filter.AuthenticationTokenFilter;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

/**
 * Security configuration.
 */
@EnableWebSecurity
@AllArgsConstructor(onConstructor = @__(@Autowired))
@EnableConfigurationProperties({AuthenticationServiceConfig.class})
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TheSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationServiceConfig authenticationServiceConfig;
    private ConfigurableJWTProcessor configurableJWTProcessor;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/v1/auth").permitAll()
                .antMatchers("/api/v1/signup").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/login**", "/signin/**",
                        "/authenticate/**", "/connect/**", "/social/authenticate").permitAll()
                .antMatchers("/webjars/springfox-swagger-ui/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/**/*.css",
                        "/**/*.html",
                        "/**/*.js",
                        "/**/*.json",
                        "/**/*.bmp",
                        "/**/*.jpeg",
                        "/**/*.jpg",
                        "/**/*.png",
                        "/**/*.ttf",
                        "/**/*.eot",
                        "/**/*.svg",
                        "/**/*.woff",
                        "/**/*.woff2").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(
                        new AuthenticationTokenFilter(internalApiClientSupplier(), jwtUtil()),
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable();
    }

    @Bean
    Supplier<InternalApiClient> internalApiClientSupplier() {
        return () -> {
            InternalApiClient internalApiClient =
                    new InternalApiClient(authorizationUrl(), restTemplate());
            return internalApiClient;
        };
    }

    @Bean
    public RestTemplate restTemplate() {
        // Do any additional configuration here
        return new RestTemplate();
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

}
