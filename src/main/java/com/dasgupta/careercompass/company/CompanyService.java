package com.dasgupta.careercompass.company;

import com.dasgupta.careercompass.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CompanyService {
    Page<CompanyDto> getAllCompanies(Pageable pageable);

    Optional<CompanyDto> getCompanyById(Integer id);

    Optional<CompanyDto> getCompanyByUserId(Integer userId);

    Optional<CompanyReviewDto> createReview(Integer companyId, Integer rating, User user);
}
