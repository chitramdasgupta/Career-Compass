package com.dasgupta.careercompass.bookmark;

import com.dasgupta.careercompass.job.Job;
import com.dasgupta.careercompass.job.JobDto;
import com.dasgupta.careercompass.job.JobMapper;
import com.dasgupta.careercompass.job.JobRepository;
import com.dasgupta.careercompass.user.Candidate;
import com.dasgupta.careercompass.user.CandidateRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    @Autowired
    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository, JobRepository jobRepository, JobMapper jobMapper, CandidateRepository candidateRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
        this.candidateRepository = candidateRepository;
    }

    public void addBookmark(Integer userId, Integer jobId) {
        Candidate candidate = candidateRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        if (!bookmarkRepository.existsByCandidateAndJob(candidate, job)) {
            Bookmark bookmark = new Bookmark().setCandidate(candidate).setJob(job);
            bookmarkRepository.save(bookmark);
        }
    }

    public void removeBookmark(Integer userId, Integer jobId) {
        Candidate candidate = candidateRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        bookmarkRepository.findByCandidateAndJob(candidate, job).ifPresent(bookmarkRepository::delete);
    }

    public List<JobDto> getBookmarkedJobs(Integer userId) {
        Candidate candidate = candidateRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        return bookmarkRepository.findByCandidate(candidate).stream().map(bookmark -> jobMapper.toDto(bookmark.getJob())).collect(Collectors.toList());
    }

    public boolean isJobBookmarked(Integer userId, Integer jobId) {
        Candidate candidate = candidateRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        return bookmarkRepository.existsByCandidateAndJob(candidate, job);
    }
}
