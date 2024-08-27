package com.dasgupta.careercompass.questionnaire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {
    private final QuestionnaireRepository questionnaireRepository;
    private final QuestionnaireQuestionMapper questionnaireQuestionMapper;

    @Autowired
    public QuestionnaireServiceImpl(QuestionnaireRepository questionnaireRepository,
                                    QuestionnaireQuestionMapper questionnaireQuestionMapper) {
        this.questionnaireRepository = questionnaireRepository;
        this.questionnaireQuestionMapper = questionnaireQuestionMapper;
    }

    @Override
    public List<QuestionnaireQuestionDto> getQuestionsByQuestionnaireId(Long id) {
        return questionnaireRepository.findById(id)
                .map(Questionnaire::getQuestionnaireQuestions)
                .map(questions -> questions.stream()
                        .map(questionnaireQuestionMapper::toDto)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new RuntimeException("Questionnaire not found"));
    }
}