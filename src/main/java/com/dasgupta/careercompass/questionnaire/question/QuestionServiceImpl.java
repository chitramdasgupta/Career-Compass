package com.dasgupta.careercompass.questionnaire.question;

import com.dasgupta.careercompass.company.CompanyServiceImpl;
import com.dasgupta.careercompass.job.JobDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {
    private static final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    @Override
    public QuestionDto getQuestionById(int id) {
        log.info("Service getQuestionById called with id={}", id);

        return questionRepository.findById(id)
                .map(question -> {
                    log.info("The found question has the id: {}", question.getId());

                    return questionMapper.toDto(question);
                })
                .orElseThrow(() -> {
                    log.info("Question not found");

                    return new EntityNotFoundException("Question not found");
                });
    }

    @Override
    public Question createQuestion(QuestionDto questionDto) {
        Question question = questionMapper.toEntity(questionDto);
        return questionRepository.save(question);
    }
}
