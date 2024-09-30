package com.dasgupta.careercompass.candidate;

import com.dasgupta.careercompass.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidateServiceImplTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private CandidateMapper candidateMapper;

    private CandidateServiceImpl candidateService;

    @BeforeEach
    void setUp() {
        candidateService = new CandidateServiceImpl(candidateRepository, candidateMapper);
    }

    @Test
    void getCandidateByUserId_WhenCandidateExists_ShouldReturnCandidateDto() {
        // Arrange
        Integer userId = 1;
        Candidate candidate = new Candidate();
        CandidateDto candidateDto = new CandidateDto();

        when(candidateRepository.findByUserId(userId)).thenReturn(Optional.of(candidate));
        when(candidateMapper.toDto(candidate)).thenReturn(candidateDto);

        // Act
        CandidateDto result = candidateService.getCandidateByUserId(userId);

        // Assert
        assertNotNull(result);
        verify(candidateRepository).findByUserId(userId);
        verify(candidateMapper).toDto(candidate);
    }

    @Test
    void getCandidateByUserId_WhenCandidateDoesNotExist_ShouldThrowEntityNotFoundException() {
        // Arrange
        Integer userId = 1;
        when(candidateRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> candidateService.getCandidateByUserId(userId));
        verify(candidateRepository).findByUserId(userId);
    }

    @Test
    void createCandidate_ShouldCreateAndReturnCandidateDto() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        Candidate candidate = new Candidate().setUser(user);
        CandidateDto candidateDto = new CandidateDto();

        when(candidateRepository.save(any(Candidate.class))).thenReturn(candidate);
        when(candidateMapper.toDto(candidate)).thenReturn(candidateDto);

        // Act
        CandidateDto result = candidateService.createCandidate(user);

        // Assert
        assertNotNull(result);
        verify(candidateRepository).save(any(Candidate.class));
        verify(candidateMapper).toDto(candidate);
    }
}
