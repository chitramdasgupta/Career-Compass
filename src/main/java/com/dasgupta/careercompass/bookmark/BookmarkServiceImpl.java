package com.dasgupta.careercompass.bookmark;

import com.dasgupta.careercompass.candidate.Candidate;
import com.dasgupta.careercompass.candidate.CandidateRepository;
import com.dasgupta.careercompass.job.Job;
import com.dasgupta.careercompass.job.JobDto;
import com.dasgupta.careercompass.job.JobMapper;
import com.dasgupta.careercompass.job.JobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Candidate candidate = candidateRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Candidate not found"));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        if (!bookmarkRepository.existsByCandidateIdAndJobId(candidate.getId(), job.getId())) {
            Bookmark bookmark = new Bookmark().setCandidate(candidate).setJob(job);
            bookmarkRepository.save(bookmark);
        }
    }

    public void removeBookmark(Integer userId, Integer jobId) {
        Candidate candidate = candidateRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Candidate not found"));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        bookmarkRepository.findByCandidateIdAndJobId(candidate.getId(), job.getId()).ifPresent(bookmarkRepository::delete);
    }

    @Override
    public Page<JobDto> getBookmarkedJobs(Integer userId, Pageable pageable) {
        Candidate candidate = candidateRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        Page<Bookmark> bookmarks = bookmarkRepository.findByCandidateId(candidate.getId(), pageable);

        return bookmarks.map(bookmark -> jobMapper.toDto(bookmark.getJob()));
    }

    public boolean isJobBookmarked(Integer userId, Integer jobId) {
        Candidate candidate = candidateRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Candidate not found"));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        return bookmarkRepository.existsByCandidateIdAndJobId(candidate.getId(), job.getId());
    }
}
