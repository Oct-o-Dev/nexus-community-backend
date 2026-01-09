package com.abhi.nexus_community.service;

import com.abhi.nexus_community.dto.PostResponse;
import com.abhi.nexus_community.dto.UserProfileResponse;
import com.abhi.nexus_community.model.Post;
import com.abhi.nexus_community.model.User;
import com.abhi.nexus_community.repository.PostLikeRepository;
import com.abhi.nexus_community.repository.PostRepository;
import com.abhi.nexus_community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime; // Use this if User doesn't have a joinedAt field yet
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public UserProfileResponse getUserProfile(String email) {
        // 1. Find User (Handle the Optional!)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Count posts
        int postCount = postRepository.countByAuthor(user);

        // 3. Count likes received
        int likesReceived = postLikeRepository.countByPostAuthor(user);

        // 4. Get their posts & Convert to DTOs
        // We need to use "findAllByAuthor" (Make sure this exists in Repository)
        List<PostResponse> myPosts = postRepository.findAllByAuthorOrderByCreatedAtDesc(user)
                .stream()
                .map(post -> mapToResponse(post, user)) // Convert each post
                .toList();

        // 5. Build Response
        return UserProfileResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .joinedAt(LocalDateTime.now()) // Placeholder (Or add createdAt to User entity)
                .postCount(postCount)
                .totalLikesReceived(likesReceived)
                .recentPosts(myPosts)
                .build();
    }

    // NEW: Get ANY user's profile by ID
    public UserProfileResponse getUserProfileById(Long userId, String viewerEmail) {
        // 1. Find the target user (The profile owner)
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Find the viewer (The person looking at the screen)
        User viewer = userRepository.findByEmail(viewerEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Viewer not found"));

        // 3. Count stats for target user
        int postCount = postRepository.countByAuthor(targetUser);
        int likesReceived = postLikeRepository.countByPostAuthor(targetUser);

        // 4. Get target user's posts
        List<PostResponse> posts = postRepository.findAllByAuthorOrderByCreatedAtDesc(targetUser)
                .stream()
                .map(post -> mapToResponse(post, viewer)) // <--- IMPORTANT: check if VIEWER liked them
                .toList();

        return UserProfileResponse.builder()
                .name(targetUser.getName())
                .email(targetUser.getEmail())
                .joinedAt(LocalDateTime.now())
                .postCount(postCount)
                .totalLikesReceived(likesReceived)
                .recentPosts(posts)
                .build();
    }

    // --- Helper to Convert Entity -> DTO ---
    private PostResponse mapToResponse(Post post, User currentUser) {
        boolean isLiked = postLikeRepository.existsByUserAndPost(currentUser, post);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .authorName(post.getAuthor().getName())
                .authorEmail(post.getAuthor().getEmail())
                .likeCount(postLikeRepository.countByPost(post))
                .likedByCurrentUser(isLiked)
                .build();
    }
}