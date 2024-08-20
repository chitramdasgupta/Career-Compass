package com.dasgupta.careercompass.job;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface JobService {
    Page<Job> getAllJobs(Pageable pageable);

    Optional<Job> getJobById(int id);
}
