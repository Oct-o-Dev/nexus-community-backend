package com.abhi.nexus_community.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT") // Allows unlimited text size
    private String content;

    private LocalDateTime createdAt;

    // --- RELATIONSHIP ---
    // Many posts belong to One User.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Creates a foreign key column in the database
    @JsonIgnore // Important: Prevents infinite loops when converting to JSON
    private User author;
}