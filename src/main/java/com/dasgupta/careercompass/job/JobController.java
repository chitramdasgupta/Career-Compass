package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.company.CompanyDto;
import com.dasgupta.careercompass.company.CompanyService;
import com.dasgupta.careercompass.constants.Constants;
import com.dasgupta.careercompass.user.Role;
import com.dasgupta.careercompass.user.User;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    private final CompanyService companyService;

    @Autowired
    public JobController(JobService jobService, CompanyService companyService) {
        this.jobService = jobService;
        this.companyService = companyService;
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

    @PostMapping("")
    public ResponseEntity<JobDto> createJob(@Valid @RequestBody JobCreateRequestDto jobDto) {
        log.info("createJob called with jobDto={}", jobDto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.ROLE_COMPANY) {
            log.info("Tried to create a job with role = {}", user.getRole());

            return ResponseEntity.badRequest().build();
        }

        CompanyDto company = companyService.getCompanyByUserId(user.getId()).get();
        jobDto.setCompanyId(company.getId());
        
        JobDto createdJob = jobService.createJob(jobDto, company.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
    }
}
