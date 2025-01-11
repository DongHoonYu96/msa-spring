package org.example.userservice.service;

import org.example.userservice.dto.UserDto;
import org.example.userservice.jpa.UserEntity;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);
    Iterable<UserEntity> getUserByAll();
}
