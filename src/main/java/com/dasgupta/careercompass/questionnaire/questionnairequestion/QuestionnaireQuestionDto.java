package com.dasgupta.careercompass.questionnaire.questionnairequestion;

import com.dasgupta.careercompass.questionnaire.question.QuestionDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionnaireQuestionDto {
    private Long id;
    private QuestionDto question;
    private Integer displayOrder;
}
