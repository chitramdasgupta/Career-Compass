package com.dasgupta.careercompass.questionnaire.answer;

import com.dasgupta.careercompass.candidate.CandidateDto;
import com.dasgupta.careercompass.candidate.CandidateMapper;
import com.dasgupta.careercompass.config.JwtAuthenticationFilter;
import com.dasgupta.careercompass.jobApplication.JobApplication;
import com.dasgupta.careercompass.questionnaire.question.QuestionDto;
import com.dasgupta.careercompass.questionnaire.question.QuestionMapper;
import com.dasgupta.careercompass.questionnaire.question.QuestionService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {
    private static final Logger log = LoggerFactory.getLogger(AnswerServiceImpl.class);

    private final AnswerRepository answerRepository;
    private final QuestionService questionService;
    private final QuestionMapper questionMapper;
    private final CandidateMapper candidateMapper;

    public AnswerServiceImpl(AnswerRepository answerRepository, QuestionService questionService,
                             QuestionMapper questionMapper, CandidateMapper candidateMapper) {
        this.answerRepository = answerRepository;
        this.questionService = questionService;
        this.questionMapper = questionMapper;
        this.candidateMapper = candidateMapper;
    }

    @Override
    public void createAnswersForJobApplication(JobApplication jobApplication, Map<Integer, String> responses, CandidateDto candidate) {
        log.info("create answers for job application with job application id: {} and candidate id: {}", jobApplication.getId(), candidate.getId());

        responses.forEach((questionId, answerResponse) -> {
            QuestionDto question = questionService.getQuestionById(questionId);

            Answer ans = new Answer()
                    .setQuestion(questionMapper.toEntity(question))
                    .setJobApplication(jobApplication)
                    .setResponse(answerResponse)
                    .setCandidate(candidateMapper.toEntity(candidate));

            answerRepository.save(ans);
        });
    }
}

