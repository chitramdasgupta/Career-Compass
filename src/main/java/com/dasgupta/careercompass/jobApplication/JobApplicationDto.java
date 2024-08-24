package com.dasgupta.careercompass.jobApplication;

import com.dasgupta.careercompass.job.JobDto;
import com.dasgupta.careercompass.user.UserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobApplicationDto {
    private Long id;
    private JobDto job;
    private UserDto user;
}
