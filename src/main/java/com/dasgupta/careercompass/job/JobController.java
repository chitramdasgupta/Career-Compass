package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.company.CompanyDto;
import com.dasgupta.careercompass.company.CompanyService;
import com.dasgupta.careercompass.constants.Constants;
import com.dasgupta.careercompass.jobApplication.JobApplicationDto;
import com.dasgupta.careercompass.questionnaire.QuestionnaireDto;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("jobs")
public class JobController {
    private static final Logger log = LoggerFactory.getLogger(JobController.class);
    private final JobService jobService;
    private final CompanyService companyService;
    private final JobRepository jobRepository;

    @Autowired
    public JobController(JobService jobService, CompanyService companyService, JobRepository jobRepository) {
        this.jobService = jobService;
        this.companyService = companyService;
        this.jobRepository = jobRepository;
    }

    @GetMapping("")
    public Page<JobDto> getAllJobs(@RequestParam(defaultValue = "" + Constants.DEFAULT_PAGE_NUMBER) int page, @RequestParam(defaultValue = "" + Constants.DEFAULT_PAGE_SIZE) int size) {
        log.info("getAllJobs called with page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        log.info("The authenticated user is {}", user);

        Page<JobDto> jobs = jobService.getAllJobs(pageable, user.getId(), user.getRole());
        log.info("The jobs are: {}", jobs);

        return jobs;
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDto> getJobById(@PathVariable int id) {
        log.info("getJobById called with id={}", id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        log.info("Authenticated user information: {}", user.toString());

        Optional<JobDto> jobDto = jobService.getJobById(id, user.getId(), user.getRole());
        log.info("jobDto={}", jobDto);

        return jobDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<JobDto> createJob(@Valid @RequestBody JobCreateRequestDto jobDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.ROLE_COMPANY) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        CompanyDto company = companyService.getCompanyByUserId(user.getId());
        jobDto.setCompanyId(company.getId());
        jobDto.setStatus(JobStatus.QUESTIONNAIRE_PENDING);
        JobDto createdJob = jobService.createJob(jobDto, company.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
    }

    @PostMapping("/{jobId}/questionnaire")
    public ResponseEntity<JobDto> createQuestionnaire(@PathVariable int jobId, @Valid @RequestBody QuestionnaireDto questionnaireDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Job job = jobRepository.findById(jobId).orElseThrow();

        if (user.getRole() != Role.ROLE_COMPANY || !Objects.equals(job.getCompany().getUser().getId(), user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        JobDto updatedJob = jobService.createQuestionnaireForJob(jobId, questionnaireDto, user.getId());
        return ResponseEntity.ok(updatedJob);
    }

    @GetMapping("/{jobId}/questionnaire")
    public ResponseEntity<QuestionnaireDto> getJobQuestionnaire(@PathVariable int jobId) {
        log.info("getJobQuestionnaire called with jobId={}", jobId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Optional<QuestionnaireDto> questionnaireDto = jobService.getJobQuestionnaire(jobId, user.getId());

        if (questionnaireDto.isPresent()) {
            log.info("Questionnaire found for jobId={}", jobId);
            return ResponseEntity.ok(questionnaireDto.get());
        } else {
            log.info("Questionnaire not found for jobId={}", jobId);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{jobId}/applications")
    public ResponseEntity<Page<JobApplicationDto>> getJobApplications(
            @PathVariable int jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("getJobApplications called with jobId={}, page={}, size={}", jobId, page, size);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.ROLE_COMPANY) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<JobApplicationDto> applications = jobService.getJobApplications(pageable, jobId, user.getId());

        return ResponseEntity.ok(applications);
    }

    @PostMapping("/{jobId}/post")
    public ResponseEntity<JobDto> postJob(@PathVariable int jobId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.ROLE_COMPANY) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        JobDto postedJob = jobService.postJob(jobId, user.getId());
        return ResponseEntity.ok(postedJob);
    }
}
