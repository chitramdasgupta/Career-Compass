package com.dasgupta.careercompass.questionnaire;

import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestionDto;

import java.util.List;

public interface QuestionnaireService {
    List<QuestionnaireQuestionDto> getQuestionsByQuestionnaireId(Long id);
}
