package com.comunityapp.converter;

import com.comunityapp.domain.User;
import com.comunityapp.dto.UserDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserConverter.class);

    public static UserDto convertToDto(User user) {
        return null;
    }
}
