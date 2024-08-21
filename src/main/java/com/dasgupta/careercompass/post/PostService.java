package com.dasgupta.careercompass.post;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostService {
    Page<Post> getAllPosts(Pageable pageable);

    Optional<Post> getPostById(int id);
}
