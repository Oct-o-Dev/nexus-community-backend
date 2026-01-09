package com.abhi.nexus_community.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    // Instead of the whole User object, we just send what we need
    private String authorName;
    private String authorEmail;
    private Long authorId;

    private int likeCount;
    private boolean likedByCurrentUser;

}