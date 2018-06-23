package com.comunityapp.security.filters;

import com.comunityapp.security.other.AccountCredentials;
import com.comunityapp.security.other.UserDetailsAdapter;
import com.comunityapp.security.service.CityAppUserDetailsService;
import com.comunityapp.security.service.TokenAuthenticationService;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    private TokenAuthenticationService tokenAuthenticationService;

    private CityAppUserDetailsService cityAppUsersDetailService;

    private static final Logger LOGGER = Logger.getLogger(JWTLoginFilter.class.getName());

    public JWTLoginFilter(String url, AuthenticationManager authManager,
                          TokenAuthenticationService tokenAuthenticationService, CityAppUserDetailsService cityAppUsersDetailService) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.cityAppUsersDetailService = cityAppUsersDetailService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        String result = new BufferedReader(new InputStreamReader(req.getInputStream())).lines()
                .collect(Collectors.joining("\n"));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
        if (result.isEmpty()) {
            logger.info("Empty Credentials");
            return getAuthenticationManager()
                    .authenticate(new UsernamePasswordAuthenticationToken("", "", Collections.emptyList()));
        }
        AccountCredentials creds = objectMapper.readValue(result, AccountCredentials.class);
        LOGGER.info("Credentials: " + creds.getUsername());
        Authentication toAuthenticate = new UsernamePasswordAuthenticationToken(creds.getUsername(),
                creds.getPassword(), Collections.emptyList());

        Authentication authenticated;
        try {
            authenticated = getAuthenticationManager().authenticate(toAuthenticate);
            LOGGER.info("atemptAuthentication result:" + authenticated);
            return authenticated;
        } catch (AuthenticationServiceException exception) {
            LOGGER.info("attemptAuthentication exception " + exception);
        }

        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) {
        LOGGER.info("success: " + auth.getName() + " authenticated");
        String role;
        UserDetailsAdapter userDetails;

        userDetails = (UserDetailsAdapter) cityAppUsersDetailService.loadUserByUsername(auth.getName());
        role = (new ArrayList(userDetails.getAuthorities())).get(0).toString();
        tokenAuthenticationService.addAuthentication(res, userDetails.getUsername(), role, userDetails.getUser().getEmail(), userDetails.getUser().getId());

    }
}