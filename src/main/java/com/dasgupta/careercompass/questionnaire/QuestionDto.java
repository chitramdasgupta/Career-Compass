package com.dasgupta.careercompass.questionnaire;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDto {
    private Long id;
    private String text;
    private QuestionType type;
}
