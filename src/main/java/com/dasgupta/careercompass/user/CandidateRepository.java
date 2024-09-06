package com.dasgupta.careercompass.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    Optional<Candidate> findByUserId(Integer userId);

    Optional<Candidate> findByUser(User user);
}