package com.dasgupta.careercompass.bookmark;

import com.dasgupta.careercompass.job.Job;
import com.dasgupta.careercompass.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUserAndJob(User user, Job job);

    List<Bookmark> findByUser(User user);

    boolean existsByUserAndJob(User user, Job job);
}