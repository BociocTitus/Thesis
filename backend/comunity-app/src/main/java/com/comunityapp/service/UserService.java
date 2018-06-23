package com.comunityapp.service;

import com.comunityapp.domain.User;
import com.comunityapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class.getName());

    private final UserRepository userRepository;

    public User saveUser(User user) {
        LOGGER.info("saveUser called:{}", user);

        user = userRepository.save(user);

        LOGGER.info("saveUser result:{}", user);
        return user;
    }

    public User findByEmail(@Email String email) {
        LOGGER.info("findByEmail called:{}", email);

        User user = userRepository.findByEmail(email);

        LOGGER.info("findByEmail result:{}", user);
        return user;
    }

    public Optional<User> findById(Long id) {
        LOGGER.info("findById called:{}",id);

        Optional<User> user = userRepository.findById(id);

        LOGGER.info("findById result:{}", user);
        return user;
    }

}
