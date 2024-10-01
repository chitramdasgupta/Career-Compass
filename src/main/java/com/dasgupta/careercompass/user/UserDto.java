package com.dasgupta.careercompass.user;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserDto {
    private Integer id;
    private String email;
    private Role role;
}
