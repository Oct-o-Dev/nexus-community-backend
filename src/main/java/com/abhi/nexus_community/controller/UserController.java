package com.abhi.nexus_community.controller;

import com.abhi.nexus_community.dto.UserProfileResponse;
import com.abhi.nexus_community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.getUserProfile(userDetails.getUsername()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> getUserProfileById(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.getUserProfileById(userId, userDetails.getUsername()));
    }
}