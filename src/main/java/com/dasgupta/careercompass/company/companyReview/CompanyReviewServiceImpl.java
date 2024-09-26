package com.dasgupta.careercompass.company.companyReview;

import com.dasgupta.careercompass.candidate.CandidateDto;
import com.dasgupta.careercompass.candidate.CandidateMapper;
import com.dasgupta.careercompass.candidate.CandidateService;
import com.dasgupta.careercompass.company.CompanyDto;
import com.dasgupta.careercompass.company.CompanyMapper;
import com.dasgupta.careercompass.company.CompanyService;
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
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;
    private final CandidateService candidateService;
    private final CandidateMapper candidateMapper;

    public CompanyReviewServiceImpl(CompanyReviewRepository companyReviewRepository,
                                    CompanyReviewMapper companyReviewMapper, CompanyService companyService,
                                    CompanyMapper companyMapper, CandidateService candidateService,
                                    CandidateMapper candidateMapper) {
        this.companyReviewRepository = companyReviewRepository;
        this.companyReviewMapper = companyReviewMapper;
        this.companyService = companyService;
        this.companyMapper = companyMapper;
        this.candidateService = candidateService;
        this.candidateMapper = candidateMapper;
    }

    @Override
    public CompanyReviewDto createReview(Integer companyId, Integer userId, Integer rating) {
        log.info("Creating review for company with id: {}, by user id: {}, rating: {}", companyId, userId, rating);

        CompanyDto company = companyService.getCompanyById(companyId);

        CandidateDto candidate = candidateService.getCandidateByUserId(userId);

        CompanyReview review = new CompanyReview()
                .setCompany(companyMapper.toEntity(company))
                .setCandidate(candidateMapper.toEntity(candidate))
                .setRating(rating);

        log.info("Saving review: {}", review);
        CompanyReview savedReview = companyReviewRepository.save(review);

        log.info("Saved review: {}", savedReview);
        return companyReviewMapper.toDto(savedReview);
    }
}
