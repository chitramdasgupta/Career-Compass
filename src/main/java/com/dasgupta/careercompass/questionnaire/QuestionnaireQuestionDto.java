package com.dasgupta.careercompass.questionnaire;

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
