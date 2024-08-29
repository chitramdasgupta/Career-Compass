package com.dasgupta.careercompass.company;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompanyDto {
    private Integer id;
    private String name;
    private String description;
    private List<CompanyReviewDto> reviews;
}
