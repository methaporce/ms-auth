package com.metaphorce.auth.services;

import com.metaphorce.databaseLib.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto user);

    List<UserDto> getAllUsers();

    List<String> getAllUserNames();


}

