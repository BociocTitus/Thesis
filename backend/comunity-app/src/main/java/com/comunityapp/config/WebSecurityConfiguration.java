package com.comunityapp.config;

import com.comunityapp.security.filters.CityAppCORSFilter;
import com.comunityapp.security.filters.JWTAuthenticationFilter;
import com.comunityapp.security.filters.JWTLoginFilter;
import com.comunityapp.security.other.CityAppAuthenticationProvider;
import com.comunityapp.security.other.CityAppLogoutSuccess;
import com.comunityapp.security.service.CityAppUserDetailsService;
import com.comunityapp.security.service.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final String REGISTER_URL = "/register";

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    private CityAppUserDetailsService cityAppUserDetailsService;

    @Autowired
    private CityAppAuthenticationProvider cityAppAuthenticationProvider;

    @Autowired
    private CityAppLogoutSuccess cityAppLogoutSuccess;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/*")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .authorizeRequests()
                .antMatchers(REGISTER_URL)
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login").permitAll().usernameParameter("username").passwordParameter("password")
                .and()
                .addFilterBefore(new CityAppCORSFilter(), ChannelProcessingFilter.class)
                .addFilterBefore(new JWTAuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTLoginFilter("/login", authenticationManager(), tokenAuthenticationService,
                                cityAppUserDetailsService)
                        , UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling();
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(cityAppAuthenticationProvider);
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}