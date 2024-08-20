package com.dasgupta.careercompass.job;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Page<Job> getAllJobs(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }

    @Override
    public Optional<Job> getJobById(int id) {
        return jobRepository.findById(id);
    }
}
