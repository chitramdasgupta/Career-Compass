package com.dasgupta.careercompass.bookmark;

import com.dasgupta.careercompass.candidate.Candidate;
import com.dasgupta.careercompass.job.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    Optional<Bookmark> findByCandidateAndJob(Candidate candidate, Job job);

    List<Bookmark> findByCandidate(Candidate candidate);

    boolean existsByCandidateAndJob(Candidate candidate, Job job);
}