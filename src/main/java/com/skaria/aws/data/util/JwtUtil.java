package com.skaria.aws.data.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.skaria.aws.security.model.JwtAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JwtUtil {

    private ConfigurableJWTProcessor configurableJWTProcessor;

    private String userNameField;
    private String groupsField;

    public JwtUtil(String userNameField, String groupsField,
                   ConfigurableJWTProcessor configurableJWTProcessor) {
        this.userNameField = userNameField;
        this.groupsField = groupsField;
        this.configurableJWTProcessor = configurableJWTProcessor;
    }

    public Authentication getAuthenticationFromToken(final String authToken) {

        final Optional<JWTClaimsSet> tokenClaims = processJwt(authToken);

        return getAuthenticationFromTokenClaims(tokenClaims);
    }


    public Optional<JWTClaimsSet> processJwt(final String token) throws SecurityException{
        try {
            final JWTClaimsSet tokenClaims = configurableJWTProcessor.process(token, null);

            return Optional.ofNullable(tokenClaims);
        } catch (ParseException | BadJOSEException | JOSEException e) {

            System.out.println("Error during processing of token");
        }
        return Optional.empty();

    }

    public User getUser(Optional<JWTClaimsSet> tokenClaims) {
        return tokenClaims.map(jwtClaimsSet -> {
            final String username = (String)jwtClaimsSet.getClaims().get(userNameField);
                final List<String> groups = (List<String>) jwtClaimsSet.getClaims().get(groupsField);
                final List<GrantedAuthority> grantedAuthorities
                        = convertList(groups, group -> new SimpleGrantedAuthority(group.toUpperCase()));

                return new User(username, "", grantedAuthorities);
        }).orElseGet(() -> null);
        /*final String username = (String)tokenClaims.getClaims().get(userNameField);

        if (Objects.nonNull(username)) {
            final List<String> groups = (List<String>) tokenClaims.getClaims().get(groupsField);
            final List<GrantedAuthority> grantedAuthorities
                    = convertList(groups, group -> new SimpleGrantedAuthority(group.toUpperCase()));

            return new User(username, "", grantedAuthorities);
        } else {
            throw new RuntimeException("Invalid token");
        }*/
    }

    private <T, U> List<U> convertList(final List<T> from, final Function<T, U> func) {
        return from.stream().map(func).collect(Collectors.toList());
    }

    private JwtAuthentication getAuthenticationFromTokenClaims(Optional<JWTClaimsSet> tokenClaims) {
        final User user = getUser(tokenClaims);
        if(user != null){
            return new JwtAuthentication(user, tokenClaims.get(), user.getAuthorities());
        }else{
            return null;
        }

    }

}
