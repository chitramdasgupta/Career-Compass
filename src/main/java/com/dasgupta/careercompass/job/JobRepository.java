package com.dasgupta.careercompass.job;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface JobRepository extends JpaRepository<Job, Integer> {
    @Override
    @NonNull
    Page<Job> findAll(@NonNull Pageable pageable);
}