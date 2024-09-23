package com.dasgupta.careercompass.jobApplication;

import com.dasgupta.careercompass.job.JobDto;
import com.dasgupta.careercompass.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("job-applications")
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;
    private final JobApplicationMapper jobApplicationMapper;

    @Autowired
    public JobApplicationController(JobApplicationService jobApplicationService, JobApplicationMapper jobApplicationMapper) {
        this.jobApplicationService = jobApplicationService;
        this.jobApplicationMapper = jobApplicationMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<JobApplicationDto> submitApplication(@RequestBody JobApplicationSubmissionDto submissionDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            JobApplication jobApplication = jobApplicationService.createJobApplication(submissionDTO, user);

            JobApplicationDto responseDto = jobApplicationMapper.toDto(jobApplication);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("")
    public ResponseEntity<Page<JobDto>> getCandidateAppliedJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            Pageable pageable = PageRequest.of(page, size);
            Page<JobDto> appliedJobs = jobApplicationService.getCandidateAppliedJobs(pageable, user.getId());

            return ResponseEntity.ok(appliedJobs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}