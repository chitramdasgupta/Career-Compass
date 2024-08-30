package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("jobs")
public class JobController {
    private static final Logger log = LoggerFactory.getLogger(JobController.class);
    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("")
    public Page<JobDto> getAllJobs(@RequestParam(defaultValue = "" + Constants.DEFAULT_PAGE_NUMBER) int page, @RequestParam(defaultValue = "" + Constants.DEFAULT_PAGE_SIZE) int size) {
        log.info("getAllJobs called with page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<JobDto> jobs = jobService.getAllJobs(pageable);
        log.info("The jobs are: {}", jobs);

        return jobs;
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDto> getJobById(@PathVariable int id) {
        log.info("getJobById called with id={}", id);
        Optional<JobDto> jobDto = jobService.getJobById(id);
        log.info("jobDto={}", jobDto);

        return jobDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
