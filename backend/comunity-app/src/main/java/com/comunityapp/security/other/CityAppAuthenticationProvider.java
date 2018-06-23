package com.comunityapp.security.other;

import com.comunityapp.security.service.CityAppUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CityAppAuthenticationProvider implements AuthenticationProvider {

    private final CityAppUserDetailsService feedyUserDetailsService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        logger.info("{}", authentication);
        UserDetailsAdapter user = (UserDetailsAdapter) feedyUserDetailsService.loadUserByUsername(username);
        logger.info("{}", user);
        if (user.getPassword().equals(password)) {
            return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
