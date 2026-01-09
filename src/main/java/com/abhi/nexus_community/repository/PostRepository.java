package com.abhi.nexus_community.repository;

import com.abhi.nexus_community.model.Post;
import com.abhi.nexus_community.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // Custom Query: Find all posts by a specific user's ID
    List<Post> findAllByAuthorId(Long userId);

    // Custom Query: Find all posts ordered by date (newest first)
    List<Post> findAllByOrderByCreatedAtDesc();

    int countByAuthor(User author);
    List<Post> findAllByAuthorOrderByCreatedAtDesc(User author);
}