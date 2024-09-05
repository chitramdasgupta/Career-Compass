package com.dasgupta.careercompass.company;

import com.dasgupta.careercompass.user.CandidateDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CompanyReviewDto {
    Integer id;
    Integer companyId;
    CandidateDto candidate;
    Integer rating;
}