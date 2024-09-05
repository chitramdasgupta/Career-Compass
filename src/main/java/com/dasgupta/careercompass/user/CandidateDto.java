package com.dasgupta.careercompass.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateDto {
    private Integer id;
    private UserDto user;
    private String firstName;
    private String middleName;
    private String lastName;
    private QualificationDegree degree;
    private String department;
}
