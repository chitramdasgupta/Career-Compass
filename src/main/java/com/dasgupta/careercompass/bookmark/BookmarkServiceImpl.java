package com.dasgupta.careercompass.bookmark;

import com.dasgupta.careercompass.job.Job;
import com.dasgupta.careercompass.job.JobDto;
import com.dasgupta.careercompass.job.JobMapper;
import com.dasgupta.careercompass.job.JobRepository;
import com.dasgupta.careercompass.user.User;
import com.dasgupta.careercompass.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    @Autowired
    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository, UserRepository userRepository, JobRepository jobRepository,
                               JobMapper jobMapper) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
    }

    public void addBookmark(Integer userId, Integer jobId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        if (!bookmarkRepository.existsByUserAndJob(user, job)) {
            Bookmark bookmark = new Bookmark().setUser(user).setJob(job);
            bookmarkRepository.save(bookmark);
        }
    }

    public void removeBookmark(Integer userId, Integer jobId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        bookmarkRepository.findByUserAndJob(user, job).ifPresent(bookmarkRepository::delete);
    }

    public List<JobDto> getBookmarkedJobs(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        
        return bookmarkRepository.findByUser(user).stream()
                .map(bookmark -> jobMapper.toDto(bookmark.getJob()))
                .collect(Collectors.toList());
    }

    public boolean isJobBookmarked(Integer userId, Integer jobId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        return bookmarkRepository.existsByUserAndJob(user, job);
    }
}
