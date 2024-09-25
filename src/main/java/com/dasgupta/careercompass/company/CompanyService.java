package com.dasgupta.careercompass.company;

import com.dasgupta.careercompass.company.companyReview.CompanyReviewDto;
import com.dasgupta.careercompass.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {
    Page<CompanyDto> getAllCompanies(Pageable pageable);

    CompanyDto getCompanyById(Integer id);

    CompanyDto getCompanyByUserId(Integer userId);
}
