package com.dasgupta.careercompass.bookmark;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    Optional<Bookmark> findByCandidateIdAndJobId(Integer candidateId, Integer jobId);

    Page<Bookmark> findByCandidateId(Integer candidateId, Pageable pageable);

    boolean existsByCandidateIdAndJobId(Integer candidateId, Integer jobId);
}
