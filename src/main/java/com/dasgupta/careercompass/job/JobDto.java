package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.company.CompanyDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
public class JobDto {
    private Integer id;
    private String title;
    private String description;
    private String minimumRequirement;
    private String desiredRequirement;
    private CompanyDto company;
    private String city;
    private String country;
    private String jobLocation;
    private BigDecimal minimumSalary;
    private BigDecimal maximumSalary;
    private String currency;
    private boolean isBookmarked;
    private JobStatus status;
}
