package com.dasgupta.careercompass.job;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface JobService {
    Page<JobDto> getAllJobs(Pageable pageable, Integer userId);

    Optional<JobDto> getJobById(int id, Integer userId);

    JobDto createJob(JobCreateRequestDto jobCreateRequestDto, Integer userId);
}
