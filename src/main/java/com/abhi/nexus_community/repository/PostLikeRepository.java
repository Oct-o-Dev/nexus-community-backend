package com.abhi.nexus_community.repository;

import com.abhi.nexus_community.model.Post;
import com.abhi.nexus_community.model.PostLike;
import com.abhi.nexus_community.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
    int countByPost(Post post);
    int countByPostAuthor(User author);
}
