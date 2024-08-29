package com.dasgupta.careercompass.questionnaire.question;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    QuestionDto toDto(Question question);

    Question toEntity(QuestionDto questionDto);
}
