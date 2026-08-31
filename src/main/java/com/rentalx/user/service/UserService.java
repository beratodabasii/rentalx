package com.rentalx.user.service;

import com.rentalx.enums.Role;
import com.rentalx.exception.InvalidCredentialsException;
import com.rentalx.security.service.JwtService;
import com.rentalx.user.dto.LoginRequest;
import com.rentalx.user.dto.LoginResponse;
import com.rentalx.user.dto.RegisterRequest;
import com.rentalx.user.dto.RegisterResponse;
import com.rentalx.user.entity.User;
import com.rentalx.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder , JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

   public RegisterResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodedPassword);
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setId(savedUser.getId());
        registerResponse.setEmail(savedUser.getEmail());
        registerResponse.setFirstName(savedUser.getFirstName());
        registerResponse.setLastName(savedUser.getLastName());
        registerResponse.setPhoneNumber(savedUser.getPhoneNumber());
        registerResponse.setRole(savedUser.getRole());
        registerResponse.setCreatedAt(savedUser.getCreatedAt());
        return registerResponse;
   }


    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken( user.getEmail(), user.getRole().name());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setUserId(user.getId());
        loginResponse.setFirstName(user.getFirstName());
        loginResponse.setLastName(user.getLastName());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setRole(user.getRole());
        return loginResponse;

    }

}
