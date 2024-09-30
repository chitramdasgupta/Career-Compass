package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.bookmark.BookmarkService;
import com.dasgupta.careercompass.company.CompanyDto;
import com.dasgupta.careercompass.company.CompanyService;
import com.dasgupta.careercompass.user.Role;
import com.dasgupta.careercompass.user.UserDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceImplTest {

    @Mock
    private JobRepository jobRepository;
    @Mock
    private JobMapper jobMapper;
    @Mock
    private BookmarkService bookmarkService;
    @Mock
    private JobCreateMapper jobCreateMapper;
    @Mock
    private LoggedInCompanyJobMapper loggedInCompanyJobMapper;
    @Mock
    private CompanyService companyService;

    private JobServiceImpl jobService;

    @BeforeEach
    void setUp() {
        jobService = new JobServiceImpl(jobRepository, jobMapper, bookmarkService, jobCreateMapper,
                loggedInCompanyJobMapper, companyService);
    }

    @Test
    void getAllJobs_ShouldReturnPageOfJobDtos() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        Integer userId = 1;
        Role role = Role.ROLE_CANDIDATE;
        Job job1 = new Job();
        job1.setId(1);
        Job job2 = new Job();
        job2.setId(2);
        Page<Job> jobPage = new PageImpl<>(Arrays.asList(job1, job2));

        when(jobRepository.findAll(pageable)).thenReturn(jobPage);
        when(bookmarkService.isJobBookmarked(userId, job1.getId())).thenReturn(true);
        when(bookmarkService.isJobBookmarked(userId, job2.getId())).thenReturn(false);
        when(jobMapper.toDto(job1, true)).thenReturn(new JobDto());
        when(jobMapper.toDto(job2, false)).thenReturn(new JobDto());

        // Act
        Page<JobDto> result = jobService.getAllJobs(pageable, userId, role);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(jobRepository).findAll(pageable);
        verify(bookmarkService, times(2)).isJobBookmarked(anyInt(), anyInt());
        verify(jobMapper, times(2)).toDto(any(Job.class), anyBoolean());
    }

    @Test
    void getJobById_WhenJobExists_ShouldReturnJobDto() {
        // Arrange
        int jobId = 1;
        Integer userId = 1;
        Job job = new Job();
        job.setId(jobId);

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(bookmarkService.isJobBookmarked(userId, jobId)).thenReturn(true);
        when(jobMapper.toDto(job, true)).thenReturn(new JobDto());

        // Act
        JobDto result = jobService.getJobById(jobId, userId);

        // Assert
        assertNotNull(result);
        verify(jobRepository).findById(jobId);
        verify(bookmarkService).isJobBookmarked(userId, jobId);
        verify(jobMapper).toDto(job, true);
    }

    @Test
    void getJobById_WhenJobDoesNotExist_ShouldThrowEntityNotFoundException() {
        // Arrange
        int jobId = 1;
        Integer userId = 1;

        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> jobService.getJobById(jobId, userId));
        verify(jobRepository).findById(jobId);
    }

    @Test
    void getJobsByCompany_ShouldReturnPageOfJobDtos() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        Integer companyId = 1;
        Job job1 = new Job();
        job1.setId(1);
        Job job2 = new Job();
        job2.setId(2);
        Page<Job> jobPage = new PageImpl<>(Arrays.asList(job1, job2));

        CompanyDto companyDto = new CompanyDto();
        UserDto userDto = new UserDto();
        userDto.setId(1);
        companyDto.setUser(userDto);

        when(jobRepository.findByCompanyId(companyId, pageable)).thenReturn(jobPage);
        when(companyService.getCompanyById(companyId)).thenReturn(companyDto);
        when(bookmarkService.isJobBookmarked(companyDto.getUser().getId(), job1.getId())).thenReturn(true);
        when(bookmarkService.isJobBookmarked(companyDto.getUser().getId(), job2.getId())).thenReturn(false);
        when(jobMapper.toDto(job1, true)).thenReturn(new JobDto());
        when(jobMapper.toDto(job2, false)).thenReturn(new JobDto());

        // Act
        Page<JobDto> result = jobService.getJobsByCompany(pageable, companyId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(jobRepository).findByCompanyId(companyId, pageable);
        verify(companyService).getCompanyById(companyId);
        verify(bookmarkService, times(2)).isJobBookmarked(anyInt(), anyInt());
        verify(jobMapper, times(2)).toDto(any(Job.class), anyBoolean());
    }

    @Test
    void getLoggedInCompanyJobs_ShouldReturnPageOfLoggedInCompanyJobDtos() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        Integer companyId = 1;
        Job job1 = new Job();
        job1.setId(1);
        Job job2 = new Job();
        job2.setId(2);
        Page<Job> jobPage = new PageImpl<>(Arrays.asList(job1, job2));

        when(jobRepository.findByCompanyId(companyId, pageable)).thenReturn(jobPage);
        when(loggedInCompanyJobMapper.toDto(job1)).thenReturn(new LoggedInCompanyJobDto());
        when(loggedInCompanyJobMapper.toDto(job2)).thenReturn(new LoggedInCompanyJobDto());

        // Act
        Page<LoggedInCompanyJobDto> result = jobService.getLoggedInCompanyJobs(pageable, companyId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(jobRepository).findByCompanyId(companyId, pageable);
        verify(loggedInCompanyJobMapper, times(2)).toDto(any(Job.class));
    }

    @Test
    void createJob_ShouldCreateAndReturnJobDto() {
        // Arrange
        JobCreateRequestDto requestDto = new JobCreateRequestDto();
        Integer companyId = 1;
        Job job = new Job();
        job.setId(1);
        JobDto jobDto = new JobDto();

        when(jobCreateMapper.toEntity(requestDto)).thenReturn(job);
        when(jobRepository.save(job)).thenReturn(job);
        when(jobMapper.toDto(job, false)).thenReturn(jobDto);

        // Act
        JobDto result = jobService.createJob(requestDto, companyId);

        // Assert
        assertNotNull(result);
        assertEquals(jobDto, result);
        verify(jobCreateMapper).toEntity(requestDto);
        verify(jobRepository).save(job);
        verify(jobMapper).toDto(job, false);
    }

    @Test
    void postJob_WhenAuthorizedAndJobReadyToPost_ShouldPostJob() {
        // Arrange
        int jobId = 1;
        Integer userId = 1;

        UserDto userDto = new UserDto();
        userDto.setId(userId);

        CompanyDto companyDto = new CompanyDto();
        companyDto.setUser(userDto);

        JobDto jobDto = new JobDto();
        jobDto.setId(jobId);
        jobDto.setCompany(companyDto);
        jobDto.setStatus(JobStatus.READY_TO_POST);

        Job postedJob = new Job();
        postedJob.setId(jobId);
        postedJob.setStatus(JobStatus.POSTED);

        JobDto postedJobDto = new JobDto();
        postedJobDto.setId(jobId);
        postedJobDto.setCompany(companyDto);
        postedJobDto.setStatus(JobStatus.POSTED);

        // Mock the getJobById method
        JobServiceImpl spyJobService = spy(jobService);
        doReturn(jobDto).when(spyJobService).getJobById(jobId, userId);

        when(jobMapper.toEntity(jobDto)).thenReturn(postedJob);
        when(jobRepository.save(postedJob)).thenReturn(postedJob);
        when(jobMapper.toDto(postedJob, false)).thenReturn(postedJobDto);
        when(bookmarkService.isJobBookmarked(userId, jobId)).thenReturn(false);

        // Act
        JobDto result = spyJobService.postJob(jobId, userId);

        // Assert
        assertNotNull(result);
        assertEquals(JobStatus.POSTED, result.getStatus());
        verify(spyJobService).getJobById(jobId, userId);
        verify(jobMapper).toEntity(jobDto);
        verify(jobRepository).save(postedJob);
        verify(jobMapper).toDto(postedJob, false);
    }

    @Test
    void postJob_WhenUnauthorized_ShouldThrowAccessDeniedException() {
        // Arrange
        int jobId = 1;
        Integer userId = 1;
        Integer unauthorizedUserId = 2;  // Different user ID

        UserDto userDto = new UserDto();
        userDto.setId(userId);

        CompanyDto companyDto = new CompanyDto();
        companyDto.setUser(userDto);

        JobDto jobDto = new JobDto();
        jobDto.setId(jobId);
        jobDto.setCompany(companyDto);
        jobDto.setStatus(JobStatus.READY_TO_POST);

        // Mock the getJobById method
        JobServiceImpl spyJobService = spy(jobService);
        doReturn(jobDto).when(spyJobService).getJobById(jobId, unauthorizedUserId);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> spyJobService.postJob(jobId, unauthorizedUserId));

        // Verify
        verify(spyJobService).getJobById(jobId, unauthorizedUserId);
        verify(jobMapper, never()).toEntity(any(JobDto.class));
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    void postJob_WhenJobNotReadyToPost_ShouldThrowValidationException() {
        // Arrange
        int jobId = 1;
        Integer userId = 1;

        UserDto userDto = new UserDto();
        userDto.setId(userId);

        CompanyDto companyDto = new CompanyDto();
        companyDto.setUser(userDto);

        JobDto jobDto = new JobDto();
        jobDto.setId(jobId);
        jobDto.setCompany(companyDto);
        jobDto.setStatus(JobStatus.QUESTIONNAIRE_PENDING);

        // Mock the getJobById method
        JobServiceImpl spyJobService = spy(jobService);
        doReturn(jobDto).when(spyJobService).getJobById(jobId, userId);

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> spyJobService.postJob(jobId, userId));

        // Additional assertions
        assertEquals("Job is not ready to post", exception.getMessage());

        // Verify
        verify(spyJobService).getJobById(jobId, userId);
        verify(jobMapper, never()).toEntity(any(JobDto.class));
        verify(jobRepository, never()).save(any(Job.class));
    }
}
