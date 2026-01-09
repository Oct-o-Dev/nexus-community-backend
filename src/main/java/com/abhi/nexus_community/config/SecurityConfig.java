package com.abhi.nexus_community.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // <--- Make sure this is imported
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    // Inject the frontend URL from application.properties / Render Env Vars
    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable CSRF (Stateless apps don't need it, and it causes 403s)
            .csrf(csrf -> csrf.disable())

            // 2. Enable CORS using our custom source below
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // 3. Define Access Rules
            .authorizeHttpRequests(auth -> auth
                // Allow Login and Register
                .requestMatchers("/api/auth/**").permitAll()
                
                // Allow WebSocket connections
                .requestMatchers("/ws/**").permitAll()
                
                // THE NUCLEAR FIX: Explicitly allow "Pre-flight" checks (OPTIONS) from the browser
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // Lock everything else
                .anyRequest().authenticated()
            )

            // 4. No Sessions (Use JWTs instead)
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 5. Add Authentication Provider and Filter
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // --- CORS CONFIGURATION ---
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ❌ REMOVE THE VARIABLE AND HARDCODE IT TEMPORARILY
        // configuration.setAllowedOrigins(List.of("http://localhost:3000", frontendUrl)); 
        
        // ✅ ADD THIS: Explicitly allow your Vercel App
        configuration.setAllowedOrigins(List.of(
            "http://localhost:3000", 
            "https://nexus-community-frontend.vercel.app" // <--- Paste your EXACT Vercel URL here
        ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}