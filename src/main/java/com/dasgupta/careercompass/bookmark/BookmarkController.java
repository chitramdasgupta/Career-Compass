package com.dasgupta.careercompass.bookmark;

import com.dasgupta.careercompass.job.JobDto;
import com.dasgupta.careercompass.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/{jobId}")
    public ResponseEntity<Void> addBookmark(@PathVariable Integer jobId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        bookmarkService.addBookmark(user.getId(), jobId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> removeBookmark(@PathVariable Integer jobId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        bookmarkService.removeBookmark(user.getId(), jobId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<List<JobDto>> getBookmarkedJobs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<JobDto> bookmarkedJobs = bookmarkService.getBookmarkedJobs(user.getId());

        return ResponseEntity.ok(bookmarkedJobs);
    }
}
