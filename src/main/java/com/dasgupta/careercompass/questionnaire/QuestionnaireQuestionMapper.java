package com.dasgupta.careercompass.questionnaire;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface QuestionnaireQuestionMapper {

    QuestionnaireQuestionDto toDto(QuestionnaireQuestion questionnaireQuestion);

    QuestionnaireQuestion toEntity(QuestionnaireQuestionDto dto);
}