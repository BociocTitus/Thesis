package com.comunityapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class.getName());

    @RequestMapping(value = "/mock", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('NORMAL','OFFICE')")
    public void register() {
        LOGGER.info("entered");
    }
}
