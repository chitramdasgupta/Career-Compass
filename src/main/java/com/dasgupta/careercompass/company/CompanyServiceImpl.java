package com.dasgupta.careercompass.company;

import com.dasgupta.careercompass.user.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {
    private static final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);
    private final CompanyRepository companyRepository;
    private final CompanyReviewRepository companyReviewRepository;
    private final CompanyMapper companyMapper;
    private final CompanyReviewMapper companyReviewMapper;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyReviewRepository companyReviewRepository, CompanyMapper companyMapper, CompanyReviewMapper companyReviewMapper) {
        this.companyRepository = companyRepository;
        this.companyReviewRepository = companyReviewRepository;
        this.companyMapper = companyMapper;
        this.companyReviewMapper = companyReviewMapper;
    }

    @Override
    public Page<CompanyDto> getAllCompanies(Pageable pageable) {
        return companyRepository.findAll(pageable).map(companyMapper::toDto);
    }

    @Override
    public Optional<CompanyDto> getCompanyById(Integer id) {
        try {
            Company company = companyRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Company not found with id %d".formatted(id)));
            log.info("Company found with id={}", id);

            CompanyDto companyDto = companyMapper.toDto(company);

            return Optional.of(companyDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<CompanyReviewDto> createReview(Integer companyId, Integer rating, User user) {
        try {
            Company company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new RuntimeException("Company not found with id: %d".formatted(companyId)));

            CompanyReview review = new CompanyReview().
                    setCompany(company).
                    setRating(rating).
                    setUser(user);
            log.info("Creating review: {}", review);

            CompanyReview savedReview = companyReviewRepository.save(review);
            return Optional.of(companyReviewMapper.toDto(savedReview));
        } catch (Exception e) {
            log.info("Could not create review: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
