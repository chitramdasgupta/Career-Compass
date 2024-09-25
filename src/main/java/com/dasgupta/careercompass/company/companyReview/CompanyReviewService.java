package com.dasgupta.careercompass.company.companyReview;

public interface CompanyReviewService {
    CompanyReviewDto createReview(Integer companyId, Integer userId, Integer rating);
}
