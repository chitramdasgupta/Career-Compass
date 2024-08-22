package com.dasgupta.careercompass.user;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.dasgupta.careercompass.user.User}
 */
@Getter
@Setter
public class LoginUserDto {
    String email;
    String password;
}
