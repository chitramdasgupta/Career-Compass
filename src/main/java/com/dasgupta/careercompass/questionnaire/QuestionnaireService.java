package com.dasgupta.careercompass.questionnaire;

import java.util.List;

public interface QuestionnaireService {
    List<QuestionnaireQuestionDto> getQuestionsByQuestionnaireId(Long id);
}
