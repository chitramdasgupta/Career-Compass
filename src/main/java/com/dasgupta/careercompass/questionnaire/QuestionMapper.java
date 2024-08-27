package com.dasgupta.careercompass.questionnaire;


public interface QuestionMapper {
    QuestionDto toDto(Question company);

    Question toEntity(QuestionDto companyDto);
}
