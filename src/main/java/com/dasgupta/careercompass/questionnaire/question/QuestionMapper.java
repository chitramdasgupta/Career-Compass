package com.dasgupta.careercompass.questionnaire.question;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    QuestionDto toDto(Question question);

    @Mapping(target = "questionnaireQuestions", ignore = true)
    Question toEntity(QuestionDto questionDto);
}
