package com.dasgupta.careercompass.bookmark;

import com.dasgupta.careercompass.job.Job;
import com.dasgupta.careercompass.user.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByCandidateAndJob(Candidate candidate, Job job);

    List<Bookmark> findByCandidate(Candidate candidate);

    boolean existsByCandidateAndJob(Candidate candidate, Job job);
}