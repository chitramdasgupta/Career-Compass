package com.dasgupta.careercompass.company;

import com.dasgupta.careercompass.user.UserDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CompanyReviewDto {
    Integer id;
    CompanyDto company;
    UserDto user;
    Integer rating;
}