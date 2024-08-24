package com.dasgupta.careercompass.jobApplication;

import com.dasgupta.careercompass.job.Job;
import com.dasgupta.careercompass.job.JobRepository;
import com.dasgupta.careercompass.questionnaire.Answer;
import com.dasgupta.careercompass.questionnaire.AnswerRepository;
import com.dasgupta.careercompass.questionnaire.Question;
import com.dasgupta.careercompass.questionnaire.QuestionRepository;
import com.dasgupta.careercompass.user.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {
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
        Job job = jobRepository.findById(submissionDTO.getJobId()).orElseThrow(() -> new RuntimeException("Job not found"));

        JobApplication jobApplication = new JobApplication();
        jobApplication.setJob(job);
        jobApplication.setUser(user);

        if (submissionDTO.getResponses() == null || submissionDTO.getResponses().isEmpty()) {
            throw new RuntimeException("No responses provided for the questionnaire");
        }

        submissionDTO.getResponses().forEach((questionId, answerResponse) -> {
            Question question = questionRepository.findById(questionId).orElse(null);

            Answer ans = new Answer();
            ans.setQuestion(question);
            ans.setJobApplication(jobApplication);
            ans.setResponse(answerResponse);
            ans.setUser(user);

            answerRepository.save(ans);
        });

        return jobApplicationRepository.save(jobApplication);
    }
}