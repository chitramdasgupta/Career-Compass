package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.company.CompanyDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class JobDto {
    private Integer id;
    private String title;
    private String description;
    private String minimumRequirement;
    private String desiredRequirement;
    private String city;
    private String country;
    private String jobLocation;
    private BigDecimal minimumSalary;
    private BigDecimal maximumSalary;
    private String currency;
    private Long questionnaireId;
    private CompanyDto company;
}
