package com.rentalx.user.controller;

import com.rentalx.user.dto.LoginRequest;
import com.rentalx.user.dto.LoginResponse;
import com.rentalx.user.dto.RegisterRequest;
import com.rentalx.user.dto.RegisterResponse;
import com.rentalx.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }



}
