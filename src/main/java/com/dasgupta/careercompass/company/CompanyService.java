package com.dasgupta.careercompass.company;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    List<Company> getAllCompanies();

    Optional<Company> getCompanyById(Integer id);
}