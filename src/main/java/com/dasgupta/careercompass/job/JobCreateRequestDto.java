package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.questionnaire.QuestionnaireDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class JobCreateRequestDto {
    private String title;
    private String description;
    private String minimumRequirement;
    private String desiredRequirement;
    private Integer companyId;
    private String city;
    private String country;
    private String jobLocation;
    private BigDecimal minimumSalary;
    private BigDecimal maximumSalary;
    private String currency;
    private QuestionnaireDto questionnaire;
}
