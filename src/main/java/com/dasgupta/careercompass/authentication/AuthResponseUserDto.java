package com.dasgupta.careercompass.authentication;

import com.dasgupta.careercompass.user.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseUserDto {
    String email;
    private Role role;
}