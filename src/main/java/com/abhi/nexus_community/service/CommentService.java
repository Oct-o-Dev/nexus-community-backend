package com.abhi.nexus_community.service;

import com.abhi.nexus_community.dto.CommentRequest;
import com.abhi.nexus_community.dto.CommentResponse;
import com.abhi.nexus_community.model.Comment;
import com.abhi.nexus_community.model.Post;
import com.abhi.nexus_community.model.User;
import com.abhi.nexus_community.repository.CommentRepository;
import com.abhi.nexus_community.repository.PostRepository;
import com.abhi.nexus_community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentResponse createComment(CommentRequest request, String userEmail) {
        // 1. Fetch User (You did this in PostService, try to copy the logic!)
        User author = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Fetch Post (Use postRepository.findById(request.getPostId()).orElseThrow(...))
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // 3. Create Comment Entity
        Comment comment = Comment.builder()
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .author(author)
                .post(post)
                .build();

        // 4. Save and Return mapped Response
        Comment savedComment = commentRepository.save(comment);
        return mapToResponse(savedComment);// Replace this
    }

    public List<CommentResponse> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // I'll help you with the mapper helper
    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .authorName(comment.getAuthor().getName())
                .build();
    }
}