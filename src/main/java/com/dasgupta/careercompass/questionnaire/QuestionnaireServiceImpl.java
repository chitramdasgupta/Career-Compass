package com.dasgupta.careercompass.questionnaire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {
    private final QuestionnaireRepository questionnaireRepository;

    @Autowired
    public QuestionnaireServiceImpl(QuestionnaireRepository questionnaireRepository) {
        this.questionnaireRepository = questionnaireRepository;
    }

    @Override
    public List<QuestionnaireQuestionDto> getQuestionsByQuestionnaireId(Long id) {
        return questionnaireRepository.findById(id)
                .map(Questionnaire::getQuestionnaireQuestions)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Questionnaire not found"));
    }

    private List<QuestionnaireQuestionDto> convertToDto(Set<QuestionnaireQuestion> questions) {
        return questions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private QuestionnaireQuestionDto mapToDto(QuestionnaireQuestion question) {
        QuestionnaireQuestionDto dto = new QuestionnaireQuestionDto();
        dto.setId(question.getId());
        dto.setText(question.getQuestion().getText());
        dto.setDisplayOrder(question.getDisplayOrder());
        return dto;
    }
}
