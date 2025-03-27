//package com.flightservice.flight_service.Security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	    return http
//	        .csrf(csrf -> csrf.disable())
//	        .authorizeHttpRequests(auth -> auth
//	            .requestMatchers("/flights/search").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
//	            .requestMatchers("/flights/add", "/flights/delete/**").hasAuthority("ROLE_ADMIN")
//	            .anyRequest().authenticated())
//	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//	        .addFilterBefore(new RoleHeaderFilter(), UsernamePasswordAuthenticationFilter.class)
//	        .build();
//	}
//
//}
//
