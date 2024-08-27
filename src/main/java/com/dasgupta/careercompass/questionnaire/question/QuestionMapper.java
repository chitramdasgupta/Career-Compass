package com.dasgupta.careercompass.questionnaire.question;


public interface QuestionMapper {
    QuestionDto toDto(Question company);

    Question toEntity(QuestionDto companyDto);
}
