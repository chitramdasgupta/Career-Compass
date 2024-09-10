package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.company.CompanyMapper;
import com.dasgupta.careercompass.questionnaire.QuestionnaireMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {QuestionnaireMapper.class, CompanyMapper.class})
public interface LoggedInCompanyJobMapper {
    @Mapping(target = "questionnaire", source = "questionnaire")
    @Mapping(target = "company", source = "company")
    LoggedInCompanyJobDto toDto(Job job);

    @Mapping(target = "jobApplications", ignore = true)
    @Mapping(target = "bookmarks", ignore = true)
    Job toEntity(LoggedInCompanyJobDto loggedInCompanyJobDto);
}
