package com.comunityapp.security.service;

import com.comunityapp.domain.User;
import com.comunityapp.repository.UserRepository;
import com.comunityapp.security.other.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityAppUserDetailsService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        logger.info("loadUserByUsername called: {}", username);
        User user = userRepository.findByEmail(username);
        if (user == null) {
            logger.info("loadByUsername failed");
            throw new UsernameNotFoundException("No such user");
        }
        logger.info("loadByUsername result:{}", user);
        return new UserDetailsAdapter(user);
    }

}
