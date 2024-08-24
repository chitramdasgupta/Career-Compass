package com.dasgupta.careercompass.questionnaire;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class QuestionnaireDtO {
    private Long id;
    private String description;
    private Set<QuestionnaireQuestionDto> questionnaireQuestions;
}
