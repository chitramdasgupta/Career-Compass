package com.dasgupta.careercompass.company;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CompanyReviewMapper.class})
public interface CompanyMapper {
    @Mapping(target = "averageRating", ignore = true)
    CompanyDto toDto(Company company);

    @Mapping(target = "reviews", ignore = true)
    Company toEntity(CompanyDto companyDto);

    @AfterMapping
    default void calculateAverageRating(@MappingTarget CompanyDto companyDto) {
        if (companyDto.getReviews() != null && !companyDto.getReviews().isEmpty()) {
            double avgRating = companyDto.getReviews().stream()
                    .mapToInt(CompanyReviewDto::getRating)
                    .average()
                    .orElse(0.0);
            companyDto.setAverageRating(avgRating);
        }
    }
}