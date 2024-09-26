package com.dasgupta.careercompass.jobApplication;

import com.dasgupta.careercompass.bookmark.BookmarkService;
import com.dasgupta.careercompass.candidate.*;
import com.dasgupta.careercompass.job.*;
import com.dasgupta.careercompass.questionnaire.answer.Answer;
import com.dasgupta.careercompass.questionnaire.answer.AnswerRepository;
import com.dasgupta.careercompass.questionnaire.answer.AnswerService;
import com.dasgupta.careercompass.questionnaire.question.*;
import com.dasgupta.careercompass.user.User;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {
    private static final Logger log = LoggerFactory.getLogger(JobApplicationServiceImpl.class);

    private final JobApplicationRepository jobApplicationRepository;
    private final AnswerService answerService;
    private final JobMapper jobMapper;
    private final JobApplicationMapper jobApplicationMapper;
    private final BookmarkService bookmarkService;
    private final JobService jobService;
    private final CandidateService candidateService;
    private final CandidateMapper candidateMapper;

    public JobApplicationServiceImpl(JobApplicationRepository jobApplicationRepository,
                                     AnswerService answerService, JobMapper jobMapper,
                                     JobApplicationMapper jobApplicationMapper, BookmarkService bookmarkService,
                                     JobService jobService, CandidateService candidateService,
                                     CandidateMapper candidateMapper) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.answerService = answerService;
        this.jobMapper = jobMapper;
        this.jobApplicationMapper = jobApplicationMapper;
        this.bookmarkService = bookmarkService;
        this.jobService = jobService;
        this.candidateService = candidateService;
        this.candidateMapper = candidateMapper;
    }

    @Override
    public Page<JobApplicationDto> getJobApplications(Pageable pageable, int jobId, int userId) {
        log.info("getJobApplications called with jobId: {}, userId: {}", jobId, userId);

        JobDto job = jobService.getJobById(jobId, userId);

        if (!Objects.equals(job.getCompany().getUser().getId(), userId)) {
            throw new AccessDeniedException("You don't have permission to view applications for this job");
        }

        Page<JobApplication> applications = jobApplicationRepository.findByJobId(jobId, pageable);
        return applications.map(jobApplicationMapper::toDto);
    }

    @Override
    public JobApplication createJobApplication(JobApplicationSubmissionDto submissionDTO, User user) {
        log.info("Creating job application for user: {}, job ID: {}", user.getId(), submissionDTO.getJobId());

        if (submissionDTO.getResponses() == null || submissionDTO.getResponses().isEmpty()) {
            throw new ValidationException("No responses provided for the questionnaire");
        }

        JobDto job = jobService.getJobById(submissionDTO.getJobId(), user.getId());

        CandidateDto candidate = candidateService.getCandidateByUserId(user.getId());

        JobApplication jobApplication = new JobApplication();
        jobApplication.setJob(jobMapper.toEntity(job));
        jobApplication.setCandidate(candidateMapper.toEntity(candidate));

        jobApplicationRepository.save(jobApplication);
        log.info("Job application saved with ID: {}", jobApplication.getId());

        answerService.createAnswersForJobApplication(jobApplication, submissionDTO.getResponses(), candidate);

        return jobApplication;
    }

    @Override
    public Page<JobDto> getCandidateAppliedJobs(Pageable pageable, Integer userId) {
        log.info("Fetching applied jobs for user: {}", userId);

        CandidateDto candidate = candidateService.getCandidateByUserId(userId);

        Page<Job> appliedJobs = jobApplicationRepository.findByCandidateId(candidate.getId(), pageable);

        log.info("Found {} applied jobs for candidate", appliedJobs.getTotalElements());
        return appliedJobs.map(job -> jobMapper.toDto(job, bookmarkService.isJobBookmarked(userId, job.getId())));
    }
}
