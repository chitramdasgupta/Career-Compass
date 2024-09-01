package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.bookmark.BookmarkService;
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

    @Autowired
    public JobServiceImpl(JobRepository jobRepository, JobMapper jobMapper, BookmarkService bookmarkService) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
        this.bookmarkService = bookmarkService;
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
}