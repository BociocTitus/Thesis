package com.comunityapp.controller;

import com.comunityapp.domain.User;
import com.comunityapp.dto.UserDto;
import com.comunityapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class.getName());

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Boolean> register(@RequestBody UserDto userDto) {
        LOGGER.info("register called:{}", userDto.getEmail(), userDto.getPassword());

        Optional<User> user = Optional.ofNullable(userService.findByEmail(userDto.getEmail()));
        if (user.isPresent()) {
            LOGGER.info("user already exists");
            return ResponseEntity.ok(false);
        }

        LOGGER.info("register success:{}", userDto);
        return ResponseEntity.ok(true);
    }
}
