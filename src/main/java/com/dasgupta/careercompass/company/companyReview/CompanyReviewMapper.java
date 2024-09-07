package com.dasgupta.careercompass.company.companyReview;

import com.dasgupta.careercompass.candidate.CandidateMapper;
import com.dasgupta.careercompass.company.Company;
import com.dasgupta.careercompass.company.CompanyMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CompanyMapper.class, CandidateMapper.class})
public interface CompanyReviewMapper {
    @Mapping(target = "companyId", source = "company.id")
    CompanyReviewDto toDto(CompanyReview companyReview);

    @Mapping(target = "company", ignore = true)
    CompanyReview toEntity(CompanyReviewDto companyReviewDto);

    @AfterMapping
    default void linkCompany(@MappingTarget CompanyReview companyReview, CompanyReviewDto dto) {
        if (dto.getCompanyId() != null) {
            Company company = new Company();
            company.setId(dto.getCompanyId());
            companyReview.setCompany(company);
        }
    }
}
