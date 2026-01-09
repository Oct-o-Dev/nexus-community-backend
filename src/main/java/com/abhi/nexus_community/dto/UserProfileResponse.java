package com.abhi.nexus_community.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UserProfileResponse {
    private String name;
    private String email;
    private LocalDateTime joinedAt;

    private int postCount;
    private int totalLikesReceived;

    // We can even send their recent posts inside the profile!
    private List<PostResponse> recentPosts;
}