package com.dasgupta.careercompass.jobapplication;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class JobApplicationSubmissionDto {
    private Integer jobId;
    private Map<Integer, String> responses;
}
