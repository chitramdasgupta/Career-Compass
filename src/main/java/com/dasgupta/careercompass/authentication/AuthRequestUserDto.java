package com.dasgupta.careercompass.authentication;

import com.dasgupta.careercompass.user.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthRequestUserDto {
    String email;

    @ToString.Exclude
    String password;

    Role role;

    String name;
}
