package com.dasgupta.careercompass.questionnaire;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionnaireQuestionDto {
    private Long id;
    private String text;
    private Integer displayOrder;
}