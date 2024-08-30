package com.dasgupta.careercompass.questionnaire.questionnairequestion;

import com.dasgupta.careercompass.questionnaire.question.QuestionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface QuestionnaireQuestionMapper {
    @Mapping(target = "question", source = "question")
    QuestionnaireQuestionDto toDto(QuestionnaireQuestion questionnaireQuestion);

    @Mapping(target = "questionnaire", ignore = true)
    QuestionnaireQuestion toEntity(QuestionnaireQuestionDto dto);
}