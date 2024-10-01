package com.dasgupta.careercompass.company;

import com.dasgupta.careercompass.company.companyReview.CompanyReviewDto;
import com.dasgupta.careercompass.company.companyReview.CompanyReviewService;
import com.dasgupta.careercompass.company.companyReview.ReviewDto;
import com.dasgupta.careercompass.job.JobDto;
import com.dasgupta.careercompass.job.JobService;
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

class CompanyControllerTest {

    @Mock
    private CompanyService companyService;
    @Mock
    private JobService jobService;
    @Mock
    private CompanyReviewService companyReviewService;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    private CompanyController companyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        companyController = new CompanyController(companyService, jobService, companyReviewService);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getAllCompanies_ShouldReturnPageOfCompanies() {
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        List<CompanyDto> companies = List.of(new CompanyDto(), new CompanyDto());
        Page<CompanyDto> companyPage = new PageImpl<>(companies);

        when(companyService.getAllCompanies(pageRequest)).thenReturn(companyPage);

        Page<CompanyDto> result = companyController.getAllCompanies(page, size);

        assertEquals(companyPage, result);
        verify(companyService).getAllCompanies(pageRequest);
    }

    @Test
    void getCompanyById_ShouldReturnCompany() {
        int companyId = 1;
        CompanyDto companyDto = new CompanyDto().setId(companyId);

        when(companyService.getCompanyById(companyId)).thenReturn(companyDto);

        ResponseEntity<CompanyDto> response = companyController.getCompanyById(companyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(companyDto, response.getBody());
        verify(companyService).getCompanyById(companyId);
    }

    @Test
    void createReview_ShouldReturnCreatedReview() {
        int companyId = 1;
        int userId = 1;
        ReviewDto reviewDto = new ReviewDto().setRating(5);
        CompanyReviewDto createdReviewDto = new CompanyReviewDto().setId(1).setRating(5);
        User user = new User().setId(userId);

        when(authentication.getPrincipal()).thenReturn(user);
        when(companyReviewService.createReview(companyId, userId, reviewDto.getRating())).thenReturn(createdReviewDto);

        ResponseEntity<CompanyReviewDto> response = companyController.createReview(companyId, reviewDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdReviewDto, response.getBody());
        verify(companyReviewService).createReview(companyId, userId, reviewDto.getRating());
    }

    @Test
    void getCompanyJobs_ShouldReturnPageOfJobs() {
        int companyId = 1;
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        List<JobDto> jobs = List.of(new JobDto(), new JobDto());
        Page<JobDto> jobPage = new PageImpl<>(jobs);

        when(jobService.getJobsByCompany(pageRequest, companyId)).thenReturn(jobPage);

        Page<JobDto> result = companyController.getCompanyJobs(companyId, page, size);

        assertEquals(jobPage, result);
        verify(jobService).getJobsByCompany(pageRequest, companyId);
    }
}
