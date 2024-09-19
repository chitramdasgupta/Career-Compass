package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.jobApplication.JobApplicationDto;
import com.dasgupta.careercompass.questionnaire.QuestionnaireDto;
import com.dasgupta.careercompass.user.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface JobService {
    Page<JobDto> getAllJobs(Pageable pageable, Integer userId, Role role);

    Optional<JobDto> getJobById(int id, Integer userId, Role role);

    Page<JobDto> getJobsByCompany(Pageable pageable, Integer companyId);

    Page<LoggedInCompanyJobDto> getLoggedInCompanyJobs(Pageable pageable, Integer companyId);

    JobDto createJob(JobCreateRequestDto jobCreateRequestDto, Integer companyId);

    JobDto createQuestionnaireForJob(int jobId, QuestionnaireDto questionnaireDto, Integer userId);

    JobDto postJob(int jobId, Integer userId);

    Optional<QuestionnaireDto> getJobQuestionnaire(int jobId, Integer id);

    Page<JobApplicationDto> getJobApplications(Pageable pageable, int jobId, int userId);
}
