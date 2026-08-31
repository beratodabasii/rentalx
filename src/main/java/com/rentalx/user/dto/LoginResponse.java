package com.rentalx.user.dto;

import com.rentalx.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private String token;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;


}
