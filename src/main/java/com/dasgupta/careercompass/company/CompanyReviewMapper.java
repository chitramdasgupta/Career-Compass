package com.dasgupta.careercompass.company;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyReviewMapper {
    CompanyReviewDto toDto(CompanyReview companyReview);

    CompanyReview toEntity(CompanyReviewDto companyReviewDto);
}
