package com.abhi.nexus_community.dto;

import lombok.Data;

@Data
public class CommentRequest {
    Long postId;
    String content;
}
