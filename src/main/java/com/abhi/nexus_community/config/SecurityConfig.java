package com.abhi.nexus_community.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value; // Import Value
import org.springframework.boot.web.servlet.FilterRegistrationBean; // <--- NEW IMPORT
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered; // <--- NEW IMPORT
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter; // <--- NEW IMPORT

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    // We keep this just in case, but the new filter is dynamic
    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF
            
            // Note: We don't need .cors() here anymore because the bean below handles it globally!
            
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/ws/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow pre-flight
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // --- 🚨 THE ULTIMATE FIX: HIGH PRIORITY CORS FILTER 🚨 ---
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow Credentials (Cookies/Auth)
        config.setAllowCredentials(true);
        
        // The "Magic Key" - Allow ALL origins dynamically
        config.setAllowedOriginPatterns(List.of("*"));
        
        // Allow ALL Headers and Methods
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        
        source.registerCorsConfiguration("/**", config);
        
        // Register the filter with HIGHEST PRECEDENCE
        // This ensures it runs BEFORE Spring Security blocks anything.
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE); 
        return bean;
    }
}