package com.dasgupta.careercompass.job;

import com.dasgupta.careercompass.company.CompanyDto;
import com.dasgupta.careercompass.questionnaire.QuestionnaireDto;
import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.CurrencyCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoggedInCompanyJobDto {
    private Integer id;
    private String title;
    private String description;
    private String minimumRequirement;
    private String desiredRequirement;
    private CompanyDto company;
    private String city;
    private CountryCode country;
    private JobLocation jobLocation;
    private BigDecimal minimumSalary;
    private BigDecimal maximumSalary;
    private CurrencyCode currency;
    private QuestionnaireDto questionnaire;
    private JobStatus status;
}
