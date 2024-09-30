package com.dasgupta.careercompass.bookmark;

import com.dasgupta.careercompass.candidate.CandidateDto;
import com.dasgupta.careercompass.candidate.CandidateMapper;
import com.dasgupta.careercompass.candidate.CandidateService;
import com.dasgupta.careercompass.job.JobDto;
import com.dasgupta.careercompass.job.JobMapper;
import com.dasgupta.careercompass.job.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceImplTest {

    @Mock
    private BookmarkRepository bookmarkRepository;
    @Mock
    private CandidateService candidateService;
    @Mock
    private JobMapper jobMapper;
    @Mock
    private JobService jobService;
    @Mock
    private CandidateMapper candidateMapper;

    private BookmarkServiceImpl bookmarkService;

    @BeforeEach
    void setUp() {
        bookmarkService = new BookmarkServiceImpl(bookmarkRepository, jobService, jobMapper, candidateService, candidateMapper);
    }

    @Test
    void addBookmark_WhenBookmarkDoesNotExist_ShouldCreateBookmark() {
        // Arrange
        Integer userId = 1;
        Integer jobId = 1;
        CandidateDto candidateDto = new CandidateDto();
        candidateDto.setId(1);
        JobDto jobDto = new JobDto();
        jobDto.setId(1);

        when(candidateService.getCandidateByUserId(userId)).thenReturn(candidateDto);
        when(jobService.getJobById(jobId, userId)).thenReturn(jobDto);
        when(bookmarkRepository.existsByCandidateIdAndJobId(candidateDto.getId(), jobDto.getId())).thenReturn(false);

        // Act
        bookmarkService.addBookmark(userId, jobId);

        // Assert
        verify(bookmarkRepository).save(any(Bookmark.class));
    }

    @Test
    void addBookmark_WhenBookmarkExists_ShouldNotCreateBookmark() {
        // Arrange
        Integer userId = 1;
        Integer jobId = 1;
        CandidateDto candidateDto = new CandidateDto();
        candidateDto.setId(1);
        JobDto jobDto = new JobDto();
        jobDto.setId(1);

        when(candidateService.getCandidateByUserId(userId)).thenReturn(candidateDto);
        when(jobService.getJobById(jobId, userId)).thenReturn(jobDto);
        when(bookmarkRepository.existsByCandidateIdAndJobId(candidateDto.getId(), jobDto.getId())).thenReturn(true);

        // Act
        bookmarkService.addBookmark(userId, jobId);

        // Assert
        verify(bookmarkRepository, never()).save(any(Bookmark.class));
    }

    @Test
    void removeBookmark_WhenBookmarkExists_ShouldDeleteBookmark() {
        // Arrange
        Integer userId = 1;
        Integer jobId = 1;
        CandidateDto candidateDto = new CandidateDto();
        candidateDto.setId(1);
        JobDto jobDto = new JobDto();
        jobDto.setId(1);
        Bookmark bookmark = new Bookmark();

        when(candidateService.getCandidateByUserId(userId)).thenReturn(candidateDto);
        when(jobService.getJobById(jobId, userId)).thenReturn(jobDto);
        when(bookmarkRepository.findByCandidateIdAndJobId(candidateDto.getId(), jobDto.getId())).thenReturn(Optional.of(bookmark));

        // Act
        bookmarkService.removeBookmark(userId, jobId);

        // Assert
        verify(bookmarkRepository).delete(bookmark);
    }

    @Test
    void removeBookmark_WhenBookmarkDoesNotExist_ShouldNotDeleteBookmark() {
        // Arrange
        Integer userId = 1;
        Integer jobId = 1;
        CandidateDto candidateDto = new CandidateDto();
        candidateDto.setId(1);
        JobDto jobDto = new JobDto();
        jobDto.setId(1);

        when(candidateService.getCandidateByUserId(userId)).thenReturn(candidateDto);
        when(jobService.getJobById(jobId, userId)).thenReturn(jobDto);
        when(bookmarkRepository.findByCandidateIdAndJobId(candidateDto.getId(), jobDto.getId())).thenReturn(Optional.empty());

        // Act
        bookmarkService.removeBookmark(userId, jobId);

        // Assert
        verify(bookmarkRepository, never()).delete(any(Bookmark.class));
    }

    @Test
    void getBookmarkedJobs_ShouldReturnPageOfJobDtos() {
        // Arrange
        Integer userId = 1;
        Pageable pageable = mock(Pageable.class);
        CandidateDto candidateDto = new CandidateDto();
        candidateDto.setId(1);
        Bookmark bookmark1 = new Bookmark();
        Bookmark bookmark2 = new Bookmark();
        Page<Bookmark> bookmarkPage = new PageImpl<>(Arrays.asList(bookmark1, bookmark2));

        when(candidateService.getCandidateByUserId(userId)).thenReturn(candidateDto);
        when(bookmarkRepository.findByCandidateId(candidateDto.getId(), pageable)).thenReturn(bookmarkPage);
        when(jobMapper.toDto(any(), eq(true))).thenReturn(new JobDto());

        // Act
        Page<JobDto> result = bookmarkService.getBookmarkedJobs(userId, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(bookmarkRepository).findByCandidateId(candidateDto.getId(), pageable);
        verify(jobMapper, times(2)).toDto(any(), eq(true));
    }

    @Test
    void isJobBookmarked_WhenBookmarkExists_ShouldReturnTrue() {
        // Arrange
        Integer userId = 1;
        Integer jobId = 1;
        CandidateDto candidateDto = new CandidateDto();
        candidateDto.setId(1);

        when(candidateService.getCandidateByUserId(userId)).thenReturn(candidateDto);
        when(bookmarkRepository.existsByCandidateIdAndJobId(candidateDto.getId(), jobId)).thenReturn(true);

        // Act
        boolean result = bookmarkService.isJobBookmarked(userId, jobId);

        // Assert
        assertTrue(result);
        verify(bookmarkRepository).existsByCandidateIdAndJobId(candidateDto.getId(), jobId);
    }

    @Test
    void isJobBookmarked_WhenBookmarkDoesNotExist_ShouldReturnFalse() {
        // Arrange
        Integer userId = 1;
        Integer jobId = 1;
        CandidateDto candidateDto = new CandidateDto();
        candidateDto.setId(1);

        when(candidateService.getCandidateByUserId(userId)).thenReturn(candidateDto);
        when(bookmarkRepository.existsByCandidateIdAndJobId(candidateDto.getId(), jobId)).thenReturn(false);

        // Act
        boolean result = bookmarkService.isJobBookmarked(userId, jobId);

        // Assert
        assertFalse(result);
        verify(bookmarkRepository).existsByCandidateIdAndJobId(candidateDto.getId(), jobId);
    }
}
