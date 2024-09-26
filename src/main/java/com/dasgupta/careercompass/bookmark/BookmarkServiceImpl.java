package com.dasgupta.careercompass.bookmark;

import com.dasgupta.careercompass.candidate.*;
import com.dasgupta.careercompass.job.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final CandidateService candidateService;
    private final JobMapper jobMapper;
    private final JobService jobService;
    private final CandidateMapper candidateMapper;

    @Autowired
    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository, JobService jobService, JobMapper jobMapper,
                               CandidateService candidateService, CandidateMapper candidateMapper) {
        this.bookmarkRepository = bookmarkRepository;
        this.jobMapper = jobMapper;
        this.candidateService = candidateService;
        this.jobService = jobService;
        this.candidateMapper = candidateMapper;
    }

    public void addBookmark(Integer userId, Integer jobId) {
        CandidateDto candidate = candidateService.getCandidateByUserId(userId);
        JobDto job = jobService.getJobById(jobId, userId);

        if (!bookmarkRepository.existsByCandidateIdAndJobId(candidate.getId(), job.getId())) {
            Bookmark bookmark = new Bookmark().setCandidate(candidateMapper.toEntity(candidate)).setJob(jobMapper.toEntity(job));
            bookmarkRepository.save(bookmark);
        }
    }

    public void removeBookmark(Integer userId, Integer jobId) {
        CandidateDto candidate = candidateService.getCandidateByUserId(userId);
        JobDto job = jobService.getJobById(jobId, userId);

        bookmarkRepository.findByCandidateIdAndJobId(candidate.getId(),
                job.getId()).ifPresent(bookmarkRepository::delete);
    }

    @Override
    public Page<JobDto> getBookmarkedJobs(Integer userId, Pageable pageable) {
        CandidateDto candidate = candidateService.getCandidateByUserId(userId);

        Page<Bookmark> bookmarks = bookmarkRepository.findByCandidateId(candidate.getId(), pageable);

        return bookmarks.map(bookmark -> jobMapper.toDto(bookmark.getJob(), true));
    }

    public boolean isJobBookmarked(Integer userId, Integer jobId) {
        CandidateDto candidate = candidateService.getCandidateByUserId(userId);
        JobDto job = jobService.getJobById(jobId, userId);

        return bookmarkRepository.existsByCandidateIdAndJobId(candidate.getId(), job.getId());
    }
}
