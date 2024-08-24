package com.dasgupta.careercompass.jobApplication;

import com.dasgupta.careercompass.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<?> create(@RequestParam Integer jobId, @AuthenticationPrincipal User user) {
        JobApplication jobApplication = jobApplicationService.createJobApplication(jobId, user);
        return ResponseEntity.ok(jobApplication);
    }
}
