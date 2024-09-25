package com.dasgupta.careercompass.company;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {
    private static final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    public Page<CompanyDto> getAllCompanies(Pageable pageable) {
        log.info("getAllCompanies called");
        return companyRepository.findAll(pageable).map(companyMapper::toDto);
    }

    @Override
    public CompanyDto getCompanyById(Integer id) {
        log.info("Searching for company with id: {}", id);

        return companyRepository.findById(id)
                .map(company -> {
                    log.info("Company found in repository: {}", company);

                    return companyMapper.toDto(company);
                })
                .orElseThrow(() -> {
                    log.info("Company not found in repository with id: {}", id);

                    return new EntityNotFoundException("Company not found with id: " + id);
                });
    }

    @Override
    public CompanyDto getCompanyByUserId(Integer userId) {
        log.info("Searching for company with user id: {}", userId);

        return companyRepository.findByUserId(userId)
                .map(companyMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Company not found for user id: " + userId));
    }
}
