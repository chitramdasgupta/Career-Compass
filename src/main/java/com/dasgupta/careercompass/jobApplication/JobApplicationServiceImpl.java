package com.dasgupta.careercompass.jobApplication;

import com.dasgupta.careercompass.candidate.Candidate;
import com.dasgupta.careercompass.candidate.CandidateRepository;
import com.dasgupta.careercompass.job.Job;
import com.dasgupta.careercompass.job.JobDto;
import com.dasgupta.careercompass.job.JobMapper;
import com.dasgupta.careercompass.job.JobRepository;
import com.dasgupta.careercompass.questionnaire.answer.Answer;
import com.dasgupta.careercompass.questionnaire.answer.AnswerRepository;
import com.dasgupta.careercompass.questionnaire.question.Question;
import com.dasgupta.careercompass.questionnaire.question.QuestionRepository;
import com.dasgupta.careercompass.user.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {
    private static final Logger log = LoggerFactory.getLogger(JobApplicationServiceImpl.class);

    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CandidateRepository candidateRepository;
    private final JobMapper jobMapper;

    @Autowired
    public JobApplicationServiceImpl(JobApplicationRepository jobApplicationRepository,
                                     JobRepository jobRepository,
                                     QuestionRepository questionRepository,
                                     AnswerRepository answerRepository,
                                     CandidateRepository candidateRepository,
                                     JobMapper jobMapper) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobRepository = jobRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.candidateRepository = candidateRepository;
        this.jobMapper = jobMapper;
    }

    @Override
    public JobApplication createJobApplication(JobApplicationSubmissionDto submissionDTO, User user) {
        log.info("Creating job application for user: {}, job ID: {}", user.getId(), submissionDTO.getJobId());

        Job job = jobRepository.findById(submissionDTO.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));
        log.info("Job found: {}", job.getId());

        Candidate candidate = candidateRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Candidate not found for user"));
        log.info("Candidate found: {}", candidate.getId());

        JobApplication jobApplication = new JobApplication();
        jobApplication.setJob(job);
        jobApplication.setCandidate(candidate);

        jobApplication = jobApplicationRepository.save(jobApplication);
        log.info("Job application saved with ID: {}", jobApplication.getId());

        if (submissionDTO.getResponses() == null || submissionDTO.getResponses().isEmpty()) {
            throw new RuntimeException("No responses provided for the questionnaire");
        }

        JobApplication finalJobApplication = jobApplication;
        submissionDTO.getResponses().forEach((questionId, answerResponse) -> {
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            Answer ans = new Answer();
            ans.setQuestion(question);
            ans.setJobApplication(finalJobApplication);
            ans.setResponse(answerResponse);
            ans.setCandidate(candidate);

            answerRepository.save(ans);
        });

        return jobApplication;
    }

    @Override
    public Page<JobDto> getCandidateAppliedJobs(Pageable pageable, Integer userId) {
        log.info("Fetching applied jobs for user: {}", userId);

        Candidate candidate = candidateRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Candidate not found for user"));

        Page<Job> appliedJobs = jobApplicationRepository.findByCandidateId(candidate.getId(), pageable);

        log.info("Found {} applied jobs for candidate", appliedJobs.getTotalElements());

        return appliedJobs.map(jobMapper::toDto);
    }
}
