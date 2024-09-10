package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.questionnaire.QuestionnaireDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface JobService {
    Page<JobDto> getAllJobs(Pageable pageable, Integer userId);

    Optional<JobDto> getJobById(int id, Integer userId);

    Page<JobDto> getJobsByCompany(Pageable pageable, Integer companyId);

    Page<LoggedInCompanyJobDto> getLoggedInCompanyJobs(Pageable pageable, Integer companyId);

    JobDto createJob(JobCreateRequestDto jobCreateRequestDto, Integer companyId);

    JobDto createQuestionnaireForJob(int jobId, QuestionnaireDto questionnaireDto, Integer userId);

    JobDto postJob(int jobId, Integer userId);

    Optional<QuestionnaireDto> getJobQuestionnaire(int jobId, Integer id);
}
