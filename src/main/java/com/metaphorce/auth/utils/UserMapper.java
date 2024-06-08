package com.metaphorce.auth.utils;


import com.metaphorce.databaseLib.dto.UserDto;
import com.metaphorce.databaseLib.models.User;

public class UserMapper {

    public static User requestToEntity(UserDto userRequestDto) {
        return User.builder()
                .id(userRequestDto.getId())
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .build();
    }


    public static UserDto entityToResponse(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

}
