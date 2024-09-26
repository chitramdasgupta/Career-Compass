package com.dasgupta.careercompass.questionnaire.question;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    public Question createQuestion(QuestionDto questionDto) {
        Question question = questionMapper.toEntity(questionDto);
        return questionRepository.save(question);
    }
}
