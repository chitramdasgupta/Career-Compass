package com.dasgupta.careercompass.Job;

import java.util.List;
import java.util.Optional;

public interface JobService {
    List<Job> getAllJobs();

    Optional<Job> getJobById(int id);
}
