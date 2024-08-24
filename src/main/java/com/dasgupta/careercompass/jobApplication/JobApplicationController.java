package com.dasgupta.careercompass.jobApplication;

import com.dasgupta.careercompass.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("job-applications/")
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    @Autowired
    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping("create")
    public ResponseEntity<?> submitApplication(@RequestBody JobApplicationSubmissionDto submissionDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        JobApplication jobApplication = jobApplicationService.createJobApplication(submissionDTO, user);
        return ResponseEntity.ok(jobApplication);
    }
}
