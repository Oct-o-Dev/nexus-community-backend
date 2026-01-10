package com.abhi.nexus_community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class TestController {

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        // If we see this on the frontend, the Network/CORS is actually fine!
        return ResponseEntity.ok("Pong! Connection is successful.");
    }
}