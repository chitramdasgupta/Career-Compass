package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.company.Company;
import com.dasgupta.careercompass.questionnaire.QuestionnaireMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {QuestionnaireMapper.class})
public interface JobCreateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", source = "companyId")
    @Mapping(target = "jobApplications", ignore = true)
    @Mapping(target = "bookmarks", ignore = true)
    @Mapping(target = "status", constant = "QUESTIONNAIRE_PENDING")
    Job toEntity(JobCreateRequestDto jobCreateRequestDto);

    default Company map(Integer companyId) {
        if (companyId == null) {
            return null;
        }
        Company company = new Company();
        company.setId(companyId);
        return company;
    }
}
