package com.dasgupta.careercompass.questionnaire.question;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDto {
    private Integer id;
    private String text;
    private QuestionType type;
}
