package com.dasgupta.careercompass.job;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class JobServiceImpl implements JobService {
    private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);
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
        log.info("jobMapper for all jobs about to be called");
        var res = jobPage.map(jobMapper::toDto);
        log.info("jobMapper called and the result is {}", res);
        return res;
    }

    @Override
    public Optional<JobDto> getJobById(int id) {
        log.info("Service getJobById called with id={}", id);
        return jobRepository.findById(id).map(jobMapper::toDto);
    }
}