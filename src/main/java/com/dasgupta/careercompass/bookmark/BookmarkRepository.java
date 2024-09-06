package com.dasgupta.careercompass.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    Optional<Bookmark> findByCandidateIdAndJobId(Integer candidateId, Integer jobId);

    List<Bookmark> findByCandidateId(Integer candidateId);

    boolean existsByCandidateIdAndJobId(Integer candidateId, Integer jobId);
}