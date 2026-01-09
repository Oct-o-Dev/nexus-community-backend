package com.abhi.nexus_community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 1. The Connection URL
        // Frontend will connect to: "http://localhost:8080/ws"
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Allow React to connect
                .withSockJS(); // Enable fallback if WebSocket isn't supported
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 2. The "Prefixes"

        // Incoming: If React sends a message to "/app/chat", it goes to our Controller.
        registry.setApplicationDestinationPrefixes("/app");

        // Outgoing: If Backend sends to "/topic/public", React receives it.
        registry.enableSimpleBroker("/topic");
    }
}