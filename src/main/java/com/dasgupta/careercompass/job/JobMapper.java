package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.company.CompanyMapper;
import com.dasgupta.careercompass.questionnaire.QuestionnaireMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CompanyMapper.class, QuestionnaireMapper.class})
public interface JobMapper {
    @Mapping(target = "company", source = "company")
    @Mapping(target = "questionnaire", source = "questionnaire")
    @Mapping(target = "isBookmarked", expression = "java(isBookmarked)")
    JobDto toDto(Job job, boolean isBookmarked);

    @Mapping(target = "jobApplications", ignore = true)
    @Mapping(target = "bookmarks", ignore = true)
    Job toEntity(JobDto jobDto);
}
