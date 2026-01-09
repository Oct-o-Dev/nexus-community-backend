package com.abhi.nexus_community.service;

import com.abhi.nexus_community.dto.PostRequest;
import com.abhi.nexus_community.dto.PostResponse;
import com.abhi.nexus_community.model.Post;
import com.abhi.nexus_community.model.User;
import com.abhi.nexus_community.repository.PostLikeRepository;
import com.abhi.nexus_community.repository.PostRepository;
import com.abhi.nexus_community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    // 1. Create a Post (Updated to return PostResponse)
    public PostResponse createPost(PostRequest request) {
        User currentUser = getCurrentUser();

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .author(currentUser)
                .build();

        Post savedPost = postRepository.save(post);
        return mapToResponse(savedPost); // <--- Convert to DTO before returning
    }

    // 2. Get All Posts (Fixed Return Type)
    public List<PostResponse> getAllPosts() { // <--- Changed from List<Post> to List<PostResponse>
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // --- Helper Method to get Logged-In User ---
    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // --- Helper to Convert Entity -> DTO ---
    private PostResponse mapToResponse(Post post) {
        User currentUser = null;
        try {
            currentUser = getCurrentUser(); // Try to get the user context
        } catch (Exception e) {
            // If user is not logged in (public view), currentUser remains null
        }

        boolean isLiked = false;
        if (currentUser != null) {
            isLiked = postLikeRepository.existsByUserAndPost(currentUser, post);
        }

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .authorName(post.getAuthor().getName())
                .authorEmail(post.getAuthor().getEmail())
                .authorId(post.getAuthor().getId())
                // --- NEW LOGIC ---
                .likeCount(postLikeRepository.countByPost(post)) // Count total likes
                .likedByCurrentUser(isLiked)                     // Check if I liked it
                .build();
    }
}