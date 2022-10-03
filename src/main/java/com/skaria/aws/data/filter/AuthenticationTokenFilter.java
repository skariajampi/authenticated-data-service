package com.skaria.aws.data.filter;

import com.skaria.aws.data.api.InternalApiClient;
import com.skaria.aws.data.util.JwtUtil;
import com.skaria.aws.security.model.JwtAuthentication;
import com.skaria.aws.security.model.UserAuthorizationResponsePayload;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.function.Supplier;

/**
 * Spring security filter for authenticating requests by token.
 */
@AllArgsConstructor
public class AuthenticationTokenFilter extends GenericFilterBean {

    private Supplier<InternalApiClient> internalApiClientSupplier;

    private JwtUtil jwtUtil;

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
                         final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;

        InternalApiClient internalApiClient = internalApiClientSupplier.get();
        try {
            ResponseEntity<UserAuthorizationResponsePayload> authorization =
                    internalApiClient.getAuthorization(httpRequest.getHeader("Authorization"));

            final UserAuthorizationResponsePayload userAuthorizationResponsePayload =
                    authorization.getBody();
            String jwtToken = userAuthorizationResponsePayload.getAuthToken();
            System.out.println("jwtToken = =====" + jwtToken);
            //use spring lib to parse jwt and extract claims etc and create Authentication object
            System.out.println("getting details from jwttoken= = =====" + jwtToken);
            JwtAuthentication jwtAuthentication = (JwtAuthentication) jwtUtil.getAuthenticationFromToken(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
        }catch(Exception e){
            System.out.println("exception ====="+ e.getMessage());
        }

        filterChain.doFilter(request, response);
        SecurityContextHolder.clearContext();
    }
}
