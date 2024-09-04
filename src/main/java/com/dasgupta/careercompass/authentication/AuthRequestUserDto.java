package com.dasgupta.careercompass.authentication;

import com.dasgupta.careercompass.user.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestUserDto {
    String email;
    String password;
    Role role;
}
