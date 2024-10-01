package com.dasgupta.careercompass.candidate;

import com.dasgupta.careercompass.user.UserDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CandidateDto {
    private Integer id;
    private UserDto user;
    private String firstName;
    private String middleName;
    private String lastName;
    private QualificationDegree degree;
    private String department;
}
