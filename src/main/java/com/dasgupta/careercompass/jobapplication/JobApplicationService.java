package com.dasgupta.careercompass.jobapplication;

import com.dasgupta.careercompass.job.JobDto;
import com.dasgupta.careercompass.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobApplicationService {
    Page<JobApplicationDto> getJobApplications(Pageable pageable, int jobId, int userId);

    JobApplication createJobApplication(JobApplicationSubmissionDto submissionDTO, User user);

    Page<JobDto> getCandidateAppliedJobs(Pageable pageable, Integer userId);
}
