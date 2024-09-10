package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.bookmark.BookmarkService;
import com.dasgupta.careercompass.company.CompanyService;
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
    private final BookmarkService bookmarkService;
    private final JobCreateMapper jobCreateMapper;
    private final CompanyService companyService;
    private final LoggedInCompanyJobMapper loggedInCompanyJobMapper;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository, JobMapper jobMapper, BookmarkService bookmarkService, JobCreateMapper jobCreateMapper, CompanyService companyService, LoggedInCompanyJobMapper loggedInCompanyJobMapper) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
        this.bookmarkService = bookmarkService;
        this.jobCreateMapper = jobCreateMapper;
        this.companyService = companyService;
        this.loggedInCompanyJobMapper = loggedInCompanyJobMapper;
    }

    @Override
    public Page<JobDto> getAllJobs(Pageable pageable, Integer userId) {
        Page<Job> jobPage = jobRepository.findAll(pageable);
        log.info("jobMapper for all jobs about to be called");

        return jobPage.map(job -> {
            JobDto jobDto = jobMapper.toDto(job);
            jobDto.setBookmarked(bookmarkService.isJobBookmarked(userId, job.getId()));

            return jobDto;
        });
    }

    @Override
    public Optional<JobDto> getJobById(int id, Integer userId) {
        log.info("Service getJobById called with id={}", id);

        Job job = jobRepository.findById(id).orElseThrow();

        JobDto dto = jobMapper.toDto(job);
        dto.setBookmarked(bookmarkService.isJobBookmarked(userId, id));

        return jobRepository.findById(id).map(jobMapper::toDto);
    }

    @Override
    public JobDto createJob(JobCreateRequestDto jobCreateRequestDto, Integer companyId) {
        log.info("Service createJob called with job dto = {}", jobCreateRequestDto);

        Job job = jobCreateMapper.toEntity(jobCreateRequestDto);
        job = jobRepository.save(job);

        return jobMapper.toDto(job);
    }

    @Override
    public Page<JobDto> getJobsByCompany(Pageable pageable, Integer companyId) {
        Page<Job> jobPage = jobRepository.findByCompanyId(companyId, pageable);

        return jobPage.map(jobMapper::toDto);
    }

    @Override
    public Page<LoggedInCompanyJobDto> getLoggedInCompanyJobs(Pageable pageable, Integer companyId) {
        Page<Job> jobPage = jobRepository.findByCompanyId(companyId, pageable);

        return jobPage.map(loggedInCompanyJobMapper::toDto);
    }

}