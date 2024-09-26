package com.dasgupta.careercompass.jobapplication;

import com.dasgupta.careercompass.candidate.CandidateDto;
import com.dasgupta.careercompass.job.JobDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobApplicationDto {
    private Integer id;
    private JobDto job;
    private CandidateDto candidate;
}

