package com.metaphorce.auth.services;


import com.metaphorce.auth.respositories.UserRepository;
import com.metaphorce.auth.utils.UserMapper;
import com.metaphorce.databaseLib.dto.UserDto;
import com.metaphorce.databaseLib.exceptions.CustomException;
import com.metaphorce.databaseLib.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDto createUser(UserDto user){
        User newUser = UserMapper.requestToEntity(user);

        if (userRepository.existsByNameOrEmail(
                newUser.getName(),
                newUser.getEmail()))
        {
            throw new CustomException(
                    "User " + newUser.getName() + " already exists");
        }

        return UserMapper.entityToResponse(userRepository.save(newUser));
    }

    public List<UserDto> getAllUsers(){

        return userRepository.findAll().stream()
                .map(UserMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllUserNames() {
        return userRepository.findAll().stream()
                .map(User::getName)
                .collect(Collectors.toList());
    }




}
