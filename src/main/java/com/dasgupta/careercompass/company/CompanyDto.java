package com.dasgupta.careercompass.company;

import com.dasgupta.careercompass.company.companyReview.CompanyReviewDto;
import com.dasgupta.careercompass.user.UserDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class CompanyDto {
    private Integer id;
    private UserDto user;
    private String name;
    private String description;
    private List<CompanyReviewDto> reviews;
    private Double averageRating;
}
