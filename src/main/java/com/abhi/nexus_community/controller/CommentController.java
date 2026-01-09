package com.abhi.nexus_community.controller;

import com.abhi.nexus_community.dto.CommentRequest;
import com.abhi.nexus_community.dto.CommentResponse;
import com.abhi.nexus_community.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetails userDetails // <--- MAGIC: Spring injects current user here!
    ) {
        // userDetails.getUsername() gives you the email
        return ResponseEntity.ok(commentService.createComment(request, userDetails.getUsername()));
    }

    // CHALLENGE: Create the GET method to fetch comments for a specific post
    // Hint: @GetMapping("/{postId}")
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
        // You will need to add a "getCommentsByPostId" method to your Service first!
    }
}