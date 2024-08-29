package com.dasgupta.careercompass.company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompanyReviewRepository extends JpaRepository<CompanyReview, Integer> {
    @Query("SELECT AVG(cr.rating) FROM CompanyReview cr WHERE cr.company.id = :companyId")
    Double getAverageRatingForCompany(Integer companyId);
}