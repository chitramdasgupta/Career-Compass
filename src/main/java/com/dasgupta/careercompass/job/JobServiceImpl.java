package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.bookmark.BookmarkService;
import com.dasgupta.careercompass.company.CompanyDto;
import com.dasgupta.careercompass.company.CompanyService;
import com.dasgupta.careercompass.user.Role;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class JobServiceImpl implements JobService {
    private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final BookmarkService bookmarkService;
    private final JobCreateMapper jobCreateMapper;
    private final LoggedInCompanyJobMapper loggedInCompanyJobMapper;
    private final CompanyService companyService;

    public JobServiceImpl(JobRepository jobRepository, JobMapper jobMapper, BookmarkService bookmarkService,
                          JobCreateMapper jobCreateMapper, LoggedInCompanyJobMapper loggedInCompanyJobMapper,
                          CompanyService companyService) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
        this.bookmarkService = bookmarkService;
        this.jobCreateMapper = jobCreateMapper;
        this.loggedInCompanyJobMapper = loggedInCompanyJobMapper;
        this.companyService = companyService;
    }

    @Override
    public Page<JobDto> getAllJobs(Pageable pageable, Integer userId, Role role) {
        log.info("getAllJobs called with userId: {}, role: {}", userId, role);

        Page<Job> jobPage = jobRepository.findAll(pageable);

        log.info("jobMapper for all jobs about to be called");
        return jobPage.map(job -> jobMapper.toDto(job, bookmarkService.isJobBookmarked(userId, job.getId())));
    }

    @Override
    public JobDto getJobById(int id, Integer userId) {
        log.info("Service getJobById called with id={}", id);

        return jobRepository.findById(id)
                .map(foundJob -> {
                    log.info("The found job has the id: {}", foundJob.getId());

                    return jobMapper.toDto(foundJob, bookmarkService.isJobBookmarked(userId, foundJob.getId()));
                })
                .orElseThrow(() -> {
                    log.info("Job not found");

                    return new EntityNotFoundException("Job not found");
                });
    }

    @Override
    public Page<JobDto> getJobsByCompany(Pageable pageable, Integer companyId) {
        log.info("Get jobs by company called with companyId: {}", companyId);

        Page<Job> jobPage = jobRepository.findByCompanyId(companyId, pageable);

        CompanyDto company = companyService.getCompanyById(companyId);

        return jobPage.map(job -> jobMapper.toDto(job,
                bookmarkService.isJobBookmarked(company.getUser().getId(), job.getId())));
    }

    @Override
    public Page<LoggedInCompanyJobDto> getLoggedInCompanyJobs(Pageable pageable, Integer companyId) {
        log.info("get logged in company jobs called with companyId: {}", companyId);

        Page<Job> jobPage = jobRepository.findByCompanyId(companyId, pageable);

        return jobPage.map(loggedInCompanyJobMapper::toDto);
    }

    @Override
    public JobDto createJob(JobCreateRequestDto jobCreateRequestDto, Integer companyId) {
        log.info("create job called with company details: {}", jobCreateRequestDto.toString());

        Job job = jobCreateMapper.toEntity(jobCreateRequestDto);

        job = jobRepository.save(job);

        return jobMapper.toDto(job, false);
    }

    @Override
    public JobDto postJob(int jobId, Integer userId) {
        log.info("post job called with jobId: {}", jobId);

        JobDto job = this.getJobById(jobId, userId);

        if (!job.getCompany().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Unauthorized access");
        }

        if (job.getStatus() != JobStatus.READY_TO_POST) {
            throw new ValidationException("Job is not ready to post");
        }

        job.setStatus(JobStatus.POSTED);

        Job savedJob = jobRepository.save(jobMapper.toEntity(job));

        return jobMapper.toDto(savedJob, bookmarkService.isJobBookmarked(userId, job.getId()));
    }
}