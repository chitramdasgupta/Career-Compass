package com.dasgupta.careercompass.job;

import java.util.List;
import java.util.Optional;

public interface JobService {
    List<Job> getAllJobs();

    Optional<Job> getJobById(int id);
}
