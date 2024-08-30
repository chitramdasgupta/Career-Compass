package com.dasgupta.careercompass.questionnaire;

import com.dasgupta.careercompass.questionnaire.questionnairequestion.QuestionnaireQuestionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {QuestionnaireQuestionMapper.class})
public interface QuestionnaireMapper {
    @Mapping(target = "questionnaireQuestions", source = "questionnaireQuestions")
    QuestionnaireDto toDto(Questionnaire questionnaire);

    @Mapping(target = "job", ignore = true)
    Questionnaire toEntity(QuestionnaireDto questionnaireDto);
}
