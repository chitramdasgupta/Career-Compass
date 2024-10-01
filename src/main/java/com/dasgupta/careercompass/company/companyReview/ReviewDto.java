package com.dasgupta.careercompass.company.companyReview;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ReviewDto {
    private Integer rating;
}
