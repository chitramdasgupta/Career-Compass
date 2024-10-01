package com.dasgupta.careercompass.bookmark;

import com.dasgupta.careercompass.job.JobDto;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookmarkControllerTest {

    @Mock
    private BookmarkService bookmarkService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private BookmarkController bookmarkController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookmarkController = new BookmarkController(bookmarkService);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void addBookmark_WhenUserIsCandidate_ShouldAddBookmark() {
        Integer jobId = 1;
        User user = new User().setId(1).setRole(Role.ROLE_CANDIDATE);

        when(authentication.getPrincipal()).thenReturn(user);

        ResponseEntity<Void> response = bookmarkController.addBookmark(jobId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookmarkService).addBookmark(user.getId(), jobId);
    }

    @Test
    void addBookmark_WhenUserIsNotCandidate_ShouldReturnBadRequest() {
        Integer jobId = 1;
        User user = new User().setId(1).setRole(Role.ROLE_COMPANY);

        when(authentication.getPrincipal()).thenReturn(user);

        ResponseEntity<Void> response = bookmarkController.addBookmark(jobId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(bookmarkService);
    }

    @Test
    void removeBookmark_WhenUserIsCandidate_ShouldRemoveBookmark() {
        Integer jobId = 1;
        User user = new User().setId(1).setRole(Role.ROLE_CANDIDATE);

        when(authentication.getPrincipal()).thenReturn(user);

        ResponseEntity<Void> response = bookmarkController.removeBookmark(jobId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookmarkService).removeBookmark(user.getId(), jobId);
    }

    @Test
    void removeBookmark_WhenUserIsNotCandidate_ShouldReturnBadRequest() {
        Integer jobId = 1;
        User user = new User().setId(1).setRole(Role.ROLE_COMPANY);

        when(authentication.getPrincipal()).thenReturn(user);

        ResponseEntity<Void> response = bookmarkController.removeBookmark(jobId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(bookmarkService);
    }

    @Test
    void getBookmarkedJobs_WhenUserIsCandidate_ShouldReturnBookmarkedJobs() {
        int page = 0;
        int size = 10;
        User user = new User().setId(1).setRole(Role.ROLE_CANDIDATE);
        PageRequest pageRequest = PageRequest.of(page, size);
        List<JobDto> jobs = List.of(new JobDto(), new JobDto());
        Page<JobDto> jobPage = new PageImpl<>(jobs);

        when(authentication.getPrincipal()).thenReturn(user);
        when(bookmarkService.getBookmarkedJobs(user.getId(), pageRequest)).thenReturn(jobPage);

        ResponseEntity<Page<JobDto>> response = bookmarkController.getBookmarkedJobs(page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobPage, response.getBody());
        verify(bookmarkService).getBookmarkedJobs(user.getId(), pageRequest);
    }

    @Test
    void getBookmarkedJobs_WhenUserIsNotCandidate_ShouldReturnBadRequest() {
        int page = 0;
        int size = 10;
        User user = new User().setId(1).setRole(Role.ROLE_COMPANY);

        when(authentication.getPrincipal()).thenReturn(user);

        ResponseEntity<Page<JobDto>> response = bookmarkController.getBookmarkedJobs(page, size);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(bookmarkService);
    }
}
