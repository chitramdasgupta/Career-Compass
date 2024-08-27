package com.dasgupta.careercompass.questionnaire;

import com.dasgupta.careercompass.questionnaire.question.QuestionMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface QuestionnaireQuestionMapper {
    QuestionnaireQuestionDto toDto(QuestionnaireQuestion questionnaireQuestion);

    QuestionnaireQuestion toEntity(QuestionnaireQuestionDto dto);
}