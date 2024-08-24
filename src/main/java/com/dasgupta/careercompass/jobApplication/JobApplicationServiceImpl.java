package com.dasgupta.careercompass.jobApplication;

import com.dasgupta.careercompass.job.Job;
import com.dasgupta.careercompass.job.JobRepository;
import com.dasgupta.careercompass.questionnaire.Answer;
import com.dasgupta.careercompass.questionnaire.AnswerRepository;
import com.dasgupta.careercompass.questionnaire.Question;
import com.dasgupta.careercompass.questionnaire.QuestionRepository;
import com.dasgupta.careercompass.user.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {
    private static final Logger log = LoggerFactory.getLogger(JobApplicationServiceImpl.class);

    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public JobApplicationServiceImpl(JobApplicationRepository jobApplicationRepository, JobRepository jobRepository, QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobRepository = jobRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public JobApplication createJobApplication(JobApplicationSubmissionDto submissionDTO, User user) {
        log.debug("Creating job application for user: {}, job ID: {}", user.getId(), submissionDTO.getJobId());

        Job job = jobRepository.findById(submissionDTO.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));
        log.debug("Job found: {}", job.getId());

        JobApplication jobApplication = new JobApplication();
        jobApplication.setJob(job);
        jobApplication.setUser(user);

        jobApplication = jobApplicationRepository.save(jobApplication);
        log.debug("Job application saved with ID: {}", jobApplication.getId());

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
            ans.setUser(user);

            answerRepository.save(ans);
        });

        return jobApplication;
    }

}