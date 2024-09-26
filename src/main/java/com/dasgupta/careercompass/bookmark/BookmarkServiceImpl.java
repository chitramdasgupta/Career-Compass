package com.dasgupta.careercompass.bookmark;

import com.dasgupta.careercompass.candidate.CandidateDto;
import com.dasgupta.careercompass.candidate.CandidateMapper;
import com.dasgupta.careercompass.candidate.CandidateService;
import com.dasgupta.careercompass.job.JobDto;
import com.dasgupta.careercompass.job.JobMapper;
import com.dasgupta.careercompass.job.JobService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BookmarkServiceImpl implements BookmarkService {
    private static final Logger log = LoggerFactory.getLogger(BookmarkServiceImpl.class);

    private final BookmarkRepository bookmarkRepository;
    private final CandidateService candidateService;
    private final JobMapper jobMapper;
    private final JobService jobService;
    private final CandidateMapper candidateMapper;

    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository, @Lazy JobService jobService, JobMapper jobMapper,
                               CandidateService candidateService, CandidateMapper candidateMapper) {
        this.bookmarkRepository = bookmarkRepository;
        this.jobMapper = jobMapper;
        this.candidateService = candidateService;
        this.jobService = jobService;
        this.candidateMapper = candidateMapper;
    }

    @Override
    public void addBookmark(Integer userId, Integer jobId) {
        log.info("add bookmark called with userId: {}, jobId: {}", userId, jobId);

        CandidateDto candidate = candidateService.getCandidateByUserId(userId);
        JobDto job = jobService.getJobById(jobId, userId);

        if (!bookmarkRepository.existsByCandidateIdAndJobId(candidate.getId(), job.getId())) {
            Bookmark bookmark = new Bookmark().setCandidate(candidateMapper.toEntity(candidate)).setJob(jobMapper.toEntity(job));
            bookmarkRepository.save(bookmark);
        }
    }

    @Override
    public void removeBookmark(Integer userId, Integer jobId) {
        log.info("remove bookmark called with userId: {} and jobId: {}", userId, jobId);

        CandidateDto candidate = candidateService.getCandidateByUserId(userId);
        JobDto job = jobService.getJobById(jobId, userId);

        bookmarkRepository.findByCandidateIdAndJobId(candidate.getId(),
                job.getId()).ifPresent(bookmarkRepository::delete);
    }

    @Override
    public Page<JobDto> getBookmarkedJobs(Integer userId, Pageable pageable) {
        log.info("get bookmarked jobs called with userId: {}", userId);

        CandidateDto candidate = candidateService.getCandidateByUserId(userId);

        Page<Bookmark> bookmarks = bookmarkRepository.findByCandidateId(candidate.getId(), pageable);

        return bookmarks.map(bookmark -> jobMapper.toDto(bookmark.getJob(), true));
    }

    @Override
    public boolean isJobBookmarked(Integer userId, Integer jobId) {
        log.info("Is job bookmarked called with userId: {}, and jobId: {}", userId, jobId);

        CandidateDto candidate = candidateService.getCandidateByUserId(userId);

        log.info("The candidate with id: {} is found", candidate.getId());
        return bookmarkRepository.existsByCandidateIdAndJobId(candidate.getId(), jobId);
    }

}
