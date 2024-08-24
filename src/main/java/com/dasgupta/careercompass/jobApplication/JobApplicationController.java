package com.dasgupta.careercompass.jobApplication;

import com.dasgupta.careercompass.job.JobDto;
import com.dasgupta.careercompass.user.User;
import com.dasgupta.careercompass.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<JobApplicationDto> submitApplication(@RequestBody JobApplicationSubmissionDto submissionDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            JobApplication jobApplication = jobApplicationService.createJobApplication(submissionDTO, user);

            JobApplicationDto responseDto = getJobApplicationDto(jobApplication, user);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private static JobApplicationDto getJobApplicationDto(JobApplication jobApplication, User user) {
        JobApplicationDto responseDto = new JobApplicationDto();
        responseDto.setId(jobApplication.getId());

        JobDto JobDto = new JobDto();
        JobDto.setId(jobApplication.getJob().getId());
        JobDto.setTitle(jobApplication.getJob().getTitle());
        JobDto.setDescription(jobApplication.getJob().getDescription());
        responseDto.setJob(JobDto);

        UserDto userResponseDto = new UserDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setEmail(user.getEmail());
        responseDto.setUser(userResponseDto);
        return responseDto;
    }

}
