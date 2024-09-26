package com.dasgupta.careercompass.jobapplication;

import com.dasgupta.careercompass.job.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer> {
    Page<JobApplication> findByJobId(int jobId, Pageable pageable);

    @Query("SELECT ja.job FROM JobApplication ja WHERE ja.candidate.id = :candidateId")
    Page<Job> findByCandidateId(@Param("candidateId") Integer candidateId, Pageable pageable);
}
