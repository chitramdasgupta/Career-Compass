package com.dasgupta.careercompass.company;

import com.dasgupta.careercompass.company.companyReview.CompanyReviewDto;
import com.dasgupta.careercompass.company.companyReview.CompanyReviewMapper;
import com.dasgupta.careercompass.user.UserMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CompanyReviewMapper.class, UserMapper.class})
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