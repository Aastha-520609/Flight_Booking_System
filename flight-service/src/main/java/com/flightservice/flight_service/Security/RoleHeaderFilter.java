//package com.flightservice.flight_service.Security;
//
//import java.io.IOException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.web.filter.OncePerRequestFilter;
//import java.util.Collections;
//
//@Component
//public class RoleHeaderFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//        
//        String role = request.getHeader("X-User-Role");
//
//        if (role != null) {
//            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
//            Authentication auth = new UsernamePasswordAuthenticationToken(null, null, Collections.singletonList(authority));
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }
//
//        chain.doFilter(request, response);
//    }
//}
