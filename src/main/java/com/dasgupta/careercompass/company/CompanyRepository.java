package com.dasgupta.careercompass.company;

import com.dasgupta.careercompass.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByUserId(Integer userId);

    Optional<Company> findByUser(User user);
}