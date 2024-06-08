package com.metaphorce.auth.controller;

import com.metaphorce.auth.services.JwtService;
import com.metaphorce.auth.services.UserInfoService;
import com.metaphorce.auth.services.UserService;
import com.metaphorce.databaseLib.dto.AuthRequest;
import com.metaphorce.databaseLib.dto.UserDto;
import com.metaphorce.databaseLib.dto.UserRequest;
import com.metaphorce.databaseLib.dto.UserResponse;
import com.metaphorce.databaseLib.models.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@Validated
public class UserSecurityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;


    /**
     * Creates a new user.
     *
     * @param userRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("Creating user: {}", userRequest);

        UserResponse createdUser = modelMapper.map(userService.createUser(modelMapper.map(userRequest, UserDto.class)), UserResponse.class);
        return ResponseEntity.ok(createdUser);
    }


    /**
     * Fetches all users.
     *
     * @return List of users.
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("Fetching all users ");
        List<UserResponse> users = userService.getAllUsers().stream()
                .map(user -> UserResponse
                        .builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build())
                .collect(Collectors.toList());
        log.info("Fetched {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "This endpoint is not secure yet";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody User userInfo) {
        userInfo.setId(UUID.randomUUID().toString());
        return service.addUser(userInfo);
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

}
