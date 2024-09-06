package com.dasgupta.careercompass.bookmark;

import com.dasgupta.careercompass.job.JobDto;
import com.dasgupta.careercompass.user.Role;
import com.dasgupta.careercompass.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {
    private static final Logger log = LoggerFactory.getLogger(BookmarkController.class);
    private final BookmarkService bookmarkService;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/{jobId}")
    public ResponseEntity<Void> addBookmark(@PathVariable Integer jobId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        log.info("The role of the requesting user is: {}", user.getRole());
        if (user.getRole() != Role.ROLE_CANDIDATE) {
            return ResponseEntity.badRequest().build();
        }

        bookmarkService.addBookmark(user.getId(), jobId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> removeBookmark(@PathVariable Integer jobId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        log.info("The role of the requesting user is: {}", user.getRole());
        if (user.getRole() != Role.ROLE_CANDIDATE) {
            return ResponseEntity.badRequest().build();
        }

        bookmarkService.removeBookmark(user.getId(), jobId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<List<JobDto>> getBookmarkedJobs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        log.info("The role of the requesting user is: {}", user.getRole());
        if (user.getRole() != Role.ROLE_CANDIDATE) {
            return ResponseEntity.badRequest().build();
        }

        List<JobDto> bookmarkedJobs = bookmarkService.getBookmarkedJobs(user.getId());

        return ResponseEntity.ok(bookmarkedJobs);
    }
}
