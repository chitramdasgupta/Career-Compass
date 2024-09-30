package com.dasgupta.careercompass.jobapplication;

import com.dasgupta.careercompass.bookmark.BookmarkService;
import com.dasgupta.careercompass.candidate.Candidate;
import com.dasgupta.careercompass.candidate.CandidateDto;
import com.dasgupta.careercompass.candidate.CandidateMapper;
import com.dasgupta.careercompass.candidate.CandidateService;
import com.dasgupta.careercompass.company.CompanyDto;
import com.dasgupta.careercompass.job.*;
import com.dasgupta.careercompass.questionnaire.answer.AnswerService;
import com.dasgupta.careercompass.user.User;
import com.dasgupta.careercompass.user.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import jakarta.validation.ValidationException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceImplTest {

    @Mock
    private JobApplicationRepository jobApplicationRepository;
    @Mock
    private AnswerService answerService;
    @Mock
    private JobMapper jobMapper;
    @Mock
    private JobApplicationMapper jobApplicationMapper;
    @Mock
    private BookmarkService bookmarkService;
    @Mock
    private JobService jobService;
    @Mock
    private CandidateService candidateService;
    @Mock
    private CandidateMapper candidateMapper;

    private JobApplicationServiceImpl jobApplicationService;

    @BeforeEach
    void setUp() {
        jobApplicationService = new JobApplicationServiceImpl(
                jobApplicationRepository, answerService, jobMapper, jobApplicationMapper,
                bookmarkService, jobService, candidateService, candidateMapper);
    }

    @Test
    void getJobApplications_WhenAuthorized_ShouldReturnApplications() {
        // Arrange
        int jobId = 1;
        int userId = 1;
        Pageable pageable = mock(Pageable.class);

        JobDto jobDto = new JobDto();
        CompanyDto companyDto = new CompanyDto();
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        companyDto.setUser(userDto);
        jobDto.setCompany(companyDto);

        when(jobService.getJobById(jobId, userId)).thenReturn(jobDto);

        JobApplication jobApplication1 = new JobApplication();
        JobApplication jobApplication2 = new JobApplication();
        Page<JobApplication> applicationPage = new PageImpl<>(Arrays.asList(jobApplication1, jobApplication2));

        when(jobApplicationRepository.findByJobId(jobId, pageable)).thenReturn(applicationPage);
        when(jobApplicationMapper.toDto(any(JobApplication.class))).thenReturn(new JobApplicationDto());

        // Act
        Page<JobApplicationDto> result = jobApplicationService.getJobApplications(pageable, jobId, userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(jobService).getJobById(jobId, userId);
        verify(jobApplicationRepository).findByJobId(jobId, pageable);
        verify(jobApplicationMapper, times(2)).toDto(any(JobApplication.class));
    }

    @Test
    void getJobApplications_WhenUnauthorized_ShouldThrowAccessDeniedException() {
        // Arrange
        int jobId = 1;
        int userId = 1;
        int unauthorizedUserId = 2;
        Pageable pageable = mock(Pageable.class);

        JobDto jobDto = new JobDto();
        CompanyDto companyDto = new CompanyDto();
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        companyDto.setUser(userDto);
        jobDto.setCompany(companyDto);

        when(jobService.getJobById(jobId, unauthorizedUserId)).thenReturn(jobDto);

        // Act & Assert
        assertThrows(AccessDeniedException.class,
                () -> jobApplicationService.getJobApplications(pageable, jobId, unauthorizedUserId));
        verify(jobService).getJobById(jobId, unauthorizedUserId);
        verify(jobApplicationRepository, never()).findByJobId(anyInt(), any(Pageable.class));
    }

    @Test
    void createJobApplication_WhenValid_ShouldCreateApplication() {
        // Arrange
        JobApplicationSubmissionDto submissionDTO = new JobApplicationSubmissionDto();
        submissionDTO.setJobId(1);
        Map<Integer, String> responses = new HashMap<>();
        responses.put(1, "Answer 1");
        responses.put(2, "Answer 2");
        submissionDTO.setResponses(responses);

        User user = new User();
        user.setId(1);

        JobDto jobDto = new JobDto();
        CandidateDto candidateDto = new CandidateDto();
        Job job = new Job();
        Candidate candidate = new Candidate();
        JobApplication jobApplication = new JobApplication();
        jobApplication.setJob(job);
        jobApplication.setCandidate(candidate);

        when(jobService.getJobById(submissionDTO.getJobId(), user.getId())).thenReturn(jobDto);
        when(candidateService.getCandidateByUserId(user.getId())).thenReturn(candidateDto);
        when(jobMapper.toEntity(jobDto)).thenReturn(job);
        when(candidateMapper.toEntity(candidateDto)).thenReturn(candidate);
        when(jobApplicationRepository.save(any(JobApplication.class))).thenReturn(jobApplication);

        ArgumentCaptor<JobApplication> jobApplicationCaptor = ArgumentCaptor.forClass(JobApplication.class);

        // Act
        JobApplication result = jobApplicationService.createJobApplication(submissionDTO, user);

        // Assert
        assertNotNull(result);
        verify(jobService).getJobById(submissionDTO.getJobId(), user.getId());
        verify(candidateService).getCandidateByUserId(user.getId());
        verify(jobApplicationRepository).save(any(JobApplication.class));
        verify(answerService).createAnswersForJobApplication(jobApplicationCaptor.capture(), eq(submissionDTO.getResponses()), eq(candidateDto));

        JobApplication capturedJobApplication = jobApplicationCaptor.getValue();
        assertNotNull(capturedJobApplication.getJob());
        assertNotNull(capturedJobApplication.getCandidate());
    }

    @Test
    void createJobApplication_WhenNoResponses_ShouldThrowValidationException() {
        // Arrange
        JobApplicationSubmissionDto submissionDTO = new JobApplicationSubmissionDto();
        submissionDTO.setJobId(1);
        submissionDTO.setResponses(Collections.emptyMap());

        User user = new User();

        // Act & Assert
        assertThrows(ValidationException.class,
                () -> jobApplicationService.createJobApplication(submissionDTO, user));
        verify(jobService, never()).getJobById(anyInt(), anyInt());
        verify(jobApplicationRepository, never()).save(any(JobApplication.class));
    }

    @Test
    void getCandidateAppliedJobs_ShouldReturnAppliedJobs() {
        // Arrange
        int userId = 1;
        Pageable pageable = mock(Pageable.class);

        CandidateDto candidateDto = new CandidateDto();
        candidateDto.setId(1);

        Job job1 = new Job();
        job1.setId(1);
        Job job2 = new Job();
        job2.setId(2);
        Page<Job> jobPage = new PageImpl<>(Arrays.asList(job1, job2));

        when(candidateService.getCandidateByUserId(userId)).thenReturn(candidateDto);
        when(jobApplicationRepository.findByCandidateId(candidateDto.getId(), pageable)).thenReturn(jobPage);
        when(bookmarkService.isJobBookmarked(userId, job1.getId())).thenReturn(true);
        when(bookmarkService.isJobBookmarked(userId, job2.getId())).thenReturn(false);
        when(jobMapper.toDto(job1, true)).thenReturn(new JobDto());
        when(jobMapper.toDto(job2, false)).thenReturn(new JobDto());

        // Act
        Page<JobDto> result = jobApplicationService.getCandidateAppliedJobs(pageable, userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(candidateService).getCandidateByUserId(userId);
        verify(jobApplicationRepository).findByCandidateId(candidateDto.getId(), pageable);
        verify(bookmarkService, times(2)).isJobBookmarked(eq(userId), anyInt());
        verify(jobMapper, times(2)).toDto(any(Job.class), anyBoolean());
    }
}
