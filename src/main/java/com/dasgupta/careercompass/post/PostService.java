package com.dasgupta.careercompass.post;


import java.util.List;
import java.util.Optional;

public interface PostService {
    List<Post> getAllPosts();

    Optional<Post> getPostById(int id);
}
