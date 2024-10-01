package com.dasgupta.careercompass.jobapplication;

import com.dasgupta.careercompass.job.JobDto;
import com.dasgupta.careercompass.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobApplicationControllerTest {

    @Mock
    private JobApplicationService jobApplicationService;

    @Mock
    private JobApplicationMapper jobApplicationMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private JobApplicationController jobApplicationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jobApplicationController = new JobApplicationController(jobApplicationService, jobApplicationMapper);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void submitApplication_WhenSuccessful_ShouldReturnJobApplicationDto() {
        JobApplicationSubmissionDto submissionDTO = new JobApplicationSubmissionDto();
        User user = new User().setId(1);
        JobApplication jobApplication = new JobApplication();
        JobApplicationDto expectedDto = new JobApplicationDto();

        when(authentication.getPrincipal()).thenReturn(user);
        when(jobApplicationService.createJobApplication(submissionDTO, user)).thenReturn(jobApplication);
        when(jobApplicationMapper.toDto(jobApplication)).thenReturn(expectedDto);

        ResponseEntity<JobApplicationDto> response = jobApplicationController.submitApplication(submissionDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDto, response.getBody());
        verify(jobApplicationService).createJobApplication(submissionDTO, user);
        verify(jobApplicationMapper).toDto(jobApplication);
    }

    @Test
    void submitApplication_WhenExceptionOccurs_ShouldReturnInternalServerError() {
        JobApplicationSubmissionDto submissionDTO = new JobApplicationSubmissionDto();
        User user = new User().setId(1);

        when(authentication.getPrincipal()).thenReturn(user);
        when(jobApplicationService.createJobApplication(submissionDTO, user)).thenThrow(new RuntimeException("Test exception"));

        ResponseEntity<JobApplicationDto> response = jobApplicationController.submitApplication(submissionDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getCandidateAppliedJobs_WhenSuccessful_ShouldReturnPageOfJobDtos() {
        int page = 0;
        int size = 10;
        User user = new User().setId(1);
        PageRequest pageRequest = PageRequest.of(page, size);
        List<JobDto> jobs = List.of(new JobDto(), new JobDto());
        Page<JobDto> jobPage = new PageImpl<>(jobs);

        when(authentication.getPrincipal()).thenReturn(user);
        when(jobApplicationService.getCandidateAppliedJobs(pageRequest, user.getId())).thenReturn(jobPage);

        ResponseEntity<Page<JobDto>> response = jobApplicationController.getCandidateAppliedJobs(page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobPage, response.getBody());
        verify(jobApplicationService).getCandidateAppliedJobs(pageRequest, user.getId());
    }

    @Test
    void getCandidateAppliedJobs_WhenExceptionOccurs_ShouldReturnInternalServerError() {
        int page = 0;
        int size = 10;
        User user = new User().setId(1);

        when(authentication.getPrincipal()).thenReturn(user);
        when(jobApplicationService.getCandidateAppliedJobs(any(PageRequest.class), eq(user.getId())))
                .thenThrow(new RuntimeException("Test exception"));

        ResponseEntity<Page<JobDto>> response = jobApplicationController.getCandidateAppliedJobs(page, size);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}
