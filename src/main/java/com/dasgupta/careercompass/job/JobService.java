package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.jobApplication.JobApplicationDto;
import com.dasgupta.careercompass.questionnaire.QuestionnaireDto;
import com.dasgupta.careercompass.user.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobService {
    Page<JobDto> getAllJobs(Pageable pageable, Integer userId, Role role);

    JobDto getJobById(int id, Integer userId);

    Page<JobDto> getJobsByCompany(Pageable pageable, Integer companyId);

    Page<LoggedInCompanyJobDto> getLoggedInCompanyJobs(Pageable pageable, Integer companyId);

    JobDto createJob(JobCreateRequestDto jobCreateRequestDto, Integer companyId);

    JobDto postJob(int jobId, Integer userId);
}
