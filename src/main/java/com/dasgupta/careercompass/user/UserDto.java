package com.dasgupta.careercompass.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Integer id;
    private String email;
    private Role role;
}
