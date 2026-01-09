package com.abhi.nexus_community.service;

import com.abhi.nexus_community.model.Post;
import com.abhi.nexus_community.model.PostLike;
import com.abhi.nexus_community.model.User;
import com.abhi.nexus_community.repository.PostLikeRepository;
import com.abhi.nexus_community.repository.PostRepository;
import com.abhi.nexus_community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void toggleLike(Long postId, String userEmail) {
        // 1. Get the Post and User
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Check if the like already exists
        Optional<PostLike> existingLike = postLikeRepository.findByUserAndPost(user, post);

        if (existingLike.isPresent()) {
            // A. If it exists -> Delete it (Unlike)
            postLikeRepository.delete(existingLike.get());
        } else {
            // B. If it doesn't exist -> Create it (Like)
            PostLike newLike = PostLike.builder()
                    .post(post)
                    .user(user)
                    .likedAt(LocalDateTime.now())
                    .build();
            postLikeRepository.save(newLike);
        }
    }
}