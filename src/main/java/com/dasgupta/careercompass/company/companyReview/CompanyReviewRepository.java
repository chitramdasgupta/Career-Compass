package com.dasgupta.careercompass.company.companyReview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyReviewRepository extends JpaRepository<CompanyReview, Integer> {
}