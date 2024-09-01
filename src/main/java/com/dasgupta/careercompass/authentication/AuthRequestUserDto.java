package com.dasgupta.careercompass.authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestUserDto {
    String email;
    String password;
}
