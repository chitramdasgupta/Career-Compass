package com.dasgupta.careercompass.company;

import com.dasgupta.careercompass.user.Candidate;
import com.dasgupta.careercompass.user.CandidateRepository;
import com.dasgupta.careercompass.user.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {
    private static final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);
    private final CompanyRepository companyRepository;
    private final CompanyReviewRepository companyReviewRepository;
    private final CompanyMapper companyMapper;
    private final CompanyReviewMapper companyReviewMapper;
    private final CandidateRepository candidateRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyReviewRepository companyReviewRepository, CompanyMapper companyMapper, CompanyReviewMapper companyReviewMapper, CandidateRepository candidateRepository) {
        this.companyRepository = companyRepository;
        this.companyReviewRepository = companyReviewRepository;
        this.companyMapper = companyMapper;
        this.companyReviewMapper = companyReviewMapper;
        this.candidateRepository = candidateRepository;
    }

    @Override
    public Page<CompanyDto> getAllCompanies(Pageable pageable) {
        return companyRepository.findAll(pageable).map(companyMapper::toDto);
    }

    @Override
    public Optional<CompanyDto> getCompanyById(Integer id) {
        log.info("Searching for company with id: {}", id);
        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            log.info("Company found in repository: {}", company.get());
            return Optional.of(companyMapper.toDto(company.get()));
        } else {
            log.warn("Company not found in repository with id: {}", id);
            // Let's try a direct database query
            List<Company> companies = companyRepository.findAll();
            log.info("All companies in database: {}", companies);
            return Optional.empty();
        }
    }


    @Override
    public Optional<CompanyDto> getCompanyByUserId(Integer userId) {
        return companyRepository.findByUserId(userId).map(companyMapper::toDto);
    }

    @Override
    public Optional<CompanyReviewDto> createReview(Integer companyId, Integer rating, User user) {
        try {
            Company company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found with id: %d".formatted(companyId)));
            Candidate candidate = candidateRepository.findByUserId(user.getId()).get();
            CompanyReview review = new CompanyReview().setCompany(company).setRating(rating).setCandidate(candidate);
            log.info("Creating review: {}", review);

            CompanyReview savedReview = companyReviewRepository.save(review);

            return Optional.of(companyReviewMapper.toDto(savedReview));
        } catch (Exception e) {
            log.error("Could not create review: {}", e.getMessage());

            return Optional.empty();
        }
    }
}
