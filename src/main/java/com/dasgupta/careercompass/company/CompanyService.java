package com.dasgupta.careercompass.company;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CompanyService {
    Page<Company> getAllCompanies(Pageable pageable);

    Optional<Company> getCompanyById(Integer id);
}
