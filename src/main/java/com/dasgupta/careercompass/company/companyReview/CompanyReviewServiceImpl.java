package com.dasgupta.careercompass.company.companyReview;

import com.dasgupta.careercompass.candidate.Candidate;
import com.dasgupta.careercompass.candidate.CandidateRepository;
import com.dasgupta.careercompass.company.Company;
import com.dasgupta.careercompass.company.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CompanyReviewServiceImpl implements CompanyReviewService {

    private static final Logger log = LoggerFactory.getLogger(CompanyReviewServiceImpl.class);

    private final CompanyReviewRepository companyReviewRepository;
    private final CompanyReviewMapper companyReviewMapper;
    private final CompanyRepository companyRepository;
    private final CandidateRepository candidateRepository;

    public CompanyReviewServiceImpl(CompanyReviewRepository companyReviewRepository,
                                    CompanyReviewMapper companyReviewMapper,
                                    CompanyRepository companyRepository,
                                    CandidateRepository candidateRepository) {
        this.companyReviewRepository = companyReviewRepository;
        this.companyReviewMapper = companyReviewMapper;
        this.companyRepository = companyRepository;
        this.candidateRepository = candidateRepository;
    }

    @Override
    public CompanyReviewDto createReview(Integer companyId, Integer userId, Integer rating) {
        log.info("Creating review for company with id: {}, by user id: {}, rating: {}", companyId, userId, rating);

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyId));

        Candidate candidate = candidateRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found for user id: " + userId));

        CompanyReview review = new CompanyReview()
                .setCompany(company)
                .setCandidate(candidate)
                .setRating(rating);

        log.info("Saving review: {}", review);
        CompanyReview savedReview = companyReviewRepository.save(review);

        log.info("Saved review: {}", savedReview);
        return companyReviewMapper.toDto(savedReview);
    }
}
