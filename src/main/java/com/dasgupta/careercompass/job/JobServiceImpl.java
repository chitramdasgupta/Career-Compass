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
    private final JobMapper jobMapper;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository, JobMapper jobMapper) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
    }

    @Override
    public Page<JobDto> getAllJobs(Pageable pageable) {
        Page<Job> jobPage = jobRepository.findAll(pageable);
        return jobPage.map(jobMapper::toDto);
    }

    @Override
    public Optional<JobDto> getJobById(int id) {
        return jobRepository.findById(id).map(jobMapper::toDto);
    }
}