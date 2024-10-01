package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.company.Company;
import com.dasgupta.careercompass.company.CompanyDto;
import com.dasgupta.careercompass.company.CompanyService;
import com.dasgupta.careercompass.jobapplication.JobApplicationDto;
import com.dasgupta.careercompass.jobapplication.JobApplicationService;
import com.dasgupta.careercompass.questionnaire.QuestionnaireDto;
import com.dasgupta.careercompass.questionnaire.QuestionnaireService;
import com.dasgupta.careercompass.user.Role;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobControllerTest {

    @Mock
    private JobService jobService;
    @Mock
    private CompanyService companyService;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private QuestionnaireService questionnaireService;
    @Mock
    private JobApplicationService jobApplicationService;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    private JobController jobController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jobController = new JobController(jobService, companyService, jobRepository, questionnaireService, jobApplicationService);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getAllJobs_ShouldReturnPageOfJobs() {
        int page = 0;
        int size = 10;
        User user = new User().setId(1).setRole(Role.ROLE_CANDIDATE);
        PageRequest pageRequest = PageRequest.of(page, size);
        List<JobDto> jobs = List.of(new JobDto(), new JobDto());
        Page<JobDto> jobPage = new PageImpl<>(jobs);

        when(authentication.getPrincipal()).thenReturn(user);
        when(jobService.getAllJobs(pageRequest, user.getId(), user.getRole())).thenReturn(jobPage);

        Page<JobDto> result = jobController.getAllJobs(page, size);

        assertEquals(jobPage, result);
        verify(jobService).getAllJobs(pageRequest, user.getId(), user.getRole());
    }

    @Test
    void getJobById_ShouldReturnJob() {
        int jobId = 1;
        User user = new User().setId(1);
        JobDto jobDto = new JobDto().setId(jobId);

        when(authentication.getPrincipal()).thenReturn(user);
        when(jobService.getJobById(jobId, user.getId())).thenReturn(jobDto);

        ResponseEntity<JobDto> response = jobController.getJobById(jobId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobDto, response.getBody());
        verify(jobService).getJobById(jobId, user.getId());
    }

    @Test
    void createJob_WhenUserIsCompany_ShouldCreateJob() {
        User user = new User().setId(1).setRole(Role.ROLE_COMPANY);
        CompanyDto companyDto = new CompanyDto().setId(1);
        JobCreateRequestDto jobCreateRequestDto = new JobCreateRequestDto();
        JobDto createdJobDto = new JobDto().setId(1);

        when(authentication.getPrincipal()).thenReturn(user);
        when(companyService.getCompanyByUserId(user.getId())).thenReturn(companyDto);
        when(jobService.createJob(any(JobCreateRequestDto.class), eq(companyDto.getId()))).thenReturn(createdJobDto);

        ResponseEntity<JobDto> response = jobController.createJob(jobCreateRequestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdJobDto, response.getBody());
        verify(jobService).createJob(any(JobCreateRequestDto.class), eq(companyDto.getId()));
    }

    @Test
    void createJob_WhenUserIsNotCompany_ShouldReturnForbidden() {
        User user = new User().setId(1).setRole(Role.ROLE_CANDIDATE);
        JobCreateRequestDto jobCreateRequestDto = new JobCreateRequestDto();

        when(authentication.getPrincipal()).thenReturn(user);

        ResponseEntity<JobDto> response = jobController.createJob(jobCreateRequestDto);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verifyNoInteractions(jobService);
    }

    @Test
    void createQuestionnaire_WhenAuthorized_ShouldCreateQuestionnaire() {
        int jobId = 1;
        User user = new User().setId(1).setRole(Role.ROLE_COMPANY);
        Job job = new Job();
        job.setCompany(new Company().setUser(user));
        QuestionnaireDto questionnaireDto = new QuestionnaireDto();
        JobDto updatedJobDto = new JobDto().setId(jobId);

        when(authentication.getPrincipal()).thenReturn(user);
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(questionnaireService.createQuestionnaireForJob(jobId, questionnaireDto, user.getId())).thenReturn(updatedJobDto);

        ResponseEntity<JobDto> response = jobController.createQuestionnaire(jobId, questionnaireDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedJobDto, response.getBody());
        verify(questionnaireService).createQuestionnaireForJob(jobId, questionnaireDto, user.getId());
    }

    @Test
    void createQuestionnaire_WhenUnauthorized_ShouldReturnForbidden() {
        int jobId = 1;
        User user = new User().setId(1).setRole(Role.ROLE_CANDIDATE);
        Job job = new Job();
        job.setCompany(new Company().setUser(new User().setId(2)));
        QuestionnaireDto questionnaireDto = new QuestionnaireDto();

        when(authentication.getPrincipal()).thenReturn(user);
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        ResponseEntity<JobDto> response = jobController.createQuestionnaire(jobId, questionnaireDto);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verifyNoInteractions(questionnaireService);
    }

    @Test
    void getJobQuestionnaire_ShouldReturnQuestionnaire() {
        int jobId = 1;
        User user = new User().setId(1);
        QuestionnaireDto questionnaireDto = new QuestionnaireDto();

        when(authentication.getPrincipal()).thenReturn(user);
        when(questionnaireService.getJobQuestionnaire(jobId, user.getId())).thenReturn(questionnaireDto);

        ResponseEntity<QuestionnaireDto> response = jobController.getJobQuestionnaire(jobId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(questionnaireDto, response.getBody());
        verify(questionnaireService).getJobQuestionnaire(jobId, user.getId());
    }

    @Test
    void getJobApplications_WhenUserIsCompany_ShouldReturnApplications() {
        int jobId = 1;
        int page = 0;
        int size = 10;
        User user = new User().setId(1).setRole(Role.ROLE_COMPANY);
        PageRequest pageRequest = PageRequest.of(page, size);
        List<JobApplicationDto> applications = List.of(new JobApplicationDto(), new JobApplicationDto());
        Page<JobApplicationDto> applicationPage = new PageImpl<>(applications);

        when(authentication.getPrincipal()).thenReturn(user);
        when(jobApplicationService.getJobApplications(pageRequest, jobId, user.getId())).thenReturn(applicationPage);

        ResponseEntity<Page<JobApplicationDto>> response = jobController.getJobApplications(jobId, page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(applicationPage, response.getBody());
        verify(jobApplicationService).getJobApplications(pageRequest, jobId, user.getId());
    }

    @Test
    void getJobApplications_WhenUserIsNotCompany_ShouldReturnForbidden() {
        int jobId = 1;
        User user = new User().setId(1).setRole(Role.ROLE_CANDIDATE);

        when(authentication.getPrincipal()).thenReturn(user);

        ResponseEntity<Page<JobApplicationDto>> response = jobController.getJobApplications(jobId, 0, 10);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verifyNoInteractions(jobApplicationService);
    }

    @Test
    void postJob_WhenUserIsCompany_ShouldPostJob() {
        int jobId = 1;
        User user = new User().setId(1).setRole(Role.ROLE_COMPANY);
        JobDto postedJobDto = new JobDto().setId(jobId);

        when(authentication.getPrincipal()).thenReturn(user);
        when(jobService.postJob(jobId, user.getId())).thenReturn(postedJobDto);

        ResponseEntity<JobDto> response = jobController.postJob(jobId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postedJobDto, response.getBody());
        verify(jobService).postJob(jobId, user.getId());
    }

    @Test
    void postJob_WhenUserIsNotCompany_ShouldReturnForbidden() {
        int jobId = 1;
        User user = new User().setId(1).setRole(Role.ROLE_CANDIDATE);

        when(authentication.getPrincipal()).thenReturn(user);

        ResponseEntity<JobDto> response = jobController.postJob(jobId);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verifyNoInteractions(jobService);
    }
}
