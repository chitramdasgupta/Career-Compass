package com.dasgupta.careercompass.jobApplication;

import com.dasgupta.careercompass.user.User;

public interface JobApplicationService {
    JobApplication createJobApplication(JobApplicationSubmissionDto submissionDTO, User user);
}
