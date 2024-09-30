package com.dasgupta.careercompass.company.companyReview;

import com.dasgupta.careercompass.candidate.Candidate;
import com.dasgupta.careercompass.candidate.CandidateDto;
import com.dasgupta.careercompass.candidate.CandidateMapper;
import com.dasgupta.careercompass.candidate.CandidateService;
import com.dasgupta.careercompass.company.Company;
import com.dasgupta.careercompass.company.CompanyDto;
import com.dasgupta.careercompass.company.CompanyMapper;
import com.dasgupta.careercompass.company.CompanyService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyReviewServiceImplTest {

    @Mock
    private CompanyReviewRepository companyReviewRepository;
    @Mock
    private CompanyReviewMapper companyReviewMapper;
    @Mock
    private CompanyService companyService;
    @Mock
    private CompanyMapper companyMapper;
    @Mock
    private CandidateService candidateService;
    @Mock
    private CandidateMapper candidateMapper;

    private CompanyReviewServiceImpl companyReviewService;

    @BeforeEach
    void setUp() {
        companyReviewService = new CompanyReviewServiceImpl(
                companyReviewRepository, companyReviewMapper, companyService,
                companyMapper, candidateService, candidateMapper
        );
    }

    @Test
    void createReview_ShouldCreateAndReturnCompanyReviewDto() {
        // Arrange
        Integer companyId = 1;
        Integer userId = 1;
        Integer rating = 5;

        CompanyDto companyDto = new CompanyDto();
        CandidateDto candidateDto = new CandidateDto();
        CompanyReview companyReview = new CompanyReview();
        CompanyReviewDto companyReviewDto = new CompanyReviewDto();

        when(companyService.getCompanyById(companyId)).thenReturn(companyDto);
        when(candidateService.getCandidateByUserId(userId)).thenReturn(candidateDto);
        when(companyMapper.toEntity(companyDto)).thenReturn(new Company());
        when(candidateMapper.toEntity(candidateDto)).thenReturn(new Candidate());
        when(companyReviewRepository.save(any(CompanyReview.class))).thenReturn(companyReview);
        when(companyReviewMapper.toDto(companyReview)).thenReturn(companyReviewDto);

        // Act
        CompanyReviewDto result = companyReviewService.createReview(companyId, userId, rating);

        // Assert
        assertNotNull(result);
        assertEquals(companyReviewDto, result);

        verify(companyService).getCompanyById(companyId);
        verify(candidateService).getCandidateByUserId(userId);
        verify(companyMapper).toEntity(companyDto);
        verify(candidateMapper).toEntity(candidateDto);
        verify(companyReviewRepository).save(any(CompanyReview.class));
        verify(companyReviewMapper).toDto(companyReview);
    }

    @Test
    void createReview_WithInvalidRating_ShouldThrowConstraintViolationException() {
        // Arrange
        Integer companyId = 1;
        Integer userId = 1;
        Integer invalidRating = 6; // Invalid rating

        CompanyDto companyDto = new CompanyDto();
        CandidateDto candidateDto = new CandidateDto();

        when(companyService.getCompanyById(companyId)).thenReturn(companyDto);
        when(candidateService.getCandidateByUserId(userId)).thenReturn(candidateDto);
        when(companyMapper.toEntity(companyDto)).thenReturn(new Company());
        when(candidateMapper.toEntity(candidateDto)).thenReturn(new Candidate());
        when(companyReviewRepository.save(any(CompanyReview.class)))
                .thenThrow(ConstraintViolationException.class);

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () ->
                companyReviewService.createReview(companyId, userId, invalidRating)
        );

        verify(companyService).getCompanyById(companyId);
        verify(candidateService).getCandidateByUserId(userId);
        verify(companyReviewRepository).save(any(CompanyReview.class));
    }

    @Test
    void createReview_WhenCompanyNotFound_ShouldThrowException() {
        // Arrange
        Integer companyId = 1;
        Integer userId = 1;
        Integer rating = 5;

        when(companyService.getCompanyById(companyId)).thenThrow(new EntityNotFoundException("Company not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                companyReviewService.createReview(companyId, userId, rating)
        );

        verify(companyService).getCompanyById(companyId);
        verify(candidateService, never()).getCandidateByUserId(anyInt());
        verify(companyReviewRepository, never()).save(any(CompanyReview.class));
    }

    @Test
    void createReview_WhenCandidateNotFound_ShouldThrowException() {
        // Arrange
        Integer companyId = 1;
        Integer userId = 1;
        Integer rating = 5;

        CompanyDto companyDto = new CompanyDto();
        when(companyService.getCompanyById(companyId)).thenReturn(companyDto);
        when(candidateService.getCandidateByUserId(userId)).thenThrow(new EntityNotFoundException("Candidate not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                companyReviewService.createReview(companyId, userId, rating)
        );

        verify(companyService).getCompanyById(companyId);
        verify(candidateService).getCandidateByUserId(userId);
        verify(companyReviewRepository, never()).save(any(CompanyReview.class));
    }
}
