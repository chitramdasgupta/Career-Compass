package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.constants.Constants;
import com.dasgupta.careercompass.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        log.info("The authenticated user is {}", user);

        Page<JobDto> jobs = jobService.getAllJobs(pageable, user.getId());
        log.info("The jobs are: {}", jobs);

        return jobs;
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDto> getJobById(@PathVariable int id) {
        log.info("getJobById called with id={}", id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Optional<JobDto> jobDto = jobService.getJobById(id, user.getId());
        log.info("jobDto={}", jobDto);

        return jobDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
