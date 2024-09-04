package com.dasgupta.careercompass.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Integer id;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private QualificationDegree degree;
    private String department;
    private Role role;
}
