package com.dasgupta.careercompass.job;

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
    private String country; // This should be a String
    private String jobLocation; // This should be a String
    private BigDecimal minimumSalary;
    private BigDecimal maximumSalary;
    private String currency; // This should be a String
    private Long questionnaireId;
}
