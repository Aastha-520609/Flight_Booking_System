package com.bookingservice.booking_service.Security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/bookings/**").access(userOnlyAccessManager())
                .anyRequest().permitAll()
            );

        return http.build();
    }

    private AuthorizationManager<RequestAuthorizationContext> userOnlyAccessManager() {
        return (authenticationSupplier, context) -> {
            HttpServletRequest request = context.getRequest();
            String role = request.getHeader("X-User-Role");
            System.out.println("Received X-User-Role in booking-service: " + role);
            boolean isUser = role != null && role.equalsIgnoreCase("USER");
            return new AuthorizationDecision(isUser);
        };
    }
}
