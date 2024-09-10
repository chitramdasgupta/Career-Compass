package com.dasgupta.careercompass.questionnaire;

import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestionDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionnaireDto {
    private Integer id;
    private String description;
    private List<QuestionnaireQuestionDto> questionnaireQuestions;
}
