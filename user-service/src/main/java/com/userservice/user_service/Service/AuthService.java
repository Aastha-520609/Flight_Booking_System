package com.userservice.user_service.Service;

import com.userservice.user_service.DTO.AuthRequest;
import com.userservice.user_service.DTO.AuthResponse;
import com.userservice.user_service.Entity.User;
import com.userservice.user_service.Repository.UserRepository;
import com.userservice.user_service.Utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser);

        return new AuthResponse(token, savedUser.getRole());
    }

//    public AuthResponse loginUser(AuthRequest authRequest) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
//        );
//
//        User user = userRepository.findByUsername(authRequest.getUsername())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        String token = jwtUtil.generateToken(user);
//        return new AuthResponse(token, user.getRole());
//    }
    
    public AuthResponse login(AuthRequest request) {
        log.info("Login attempt for username: {}", request.getUsername());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            log.info("Authentication successful for username: {}", request.getUsername());
            
            User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            
            String token = jwtUtil.generateToken(user);
            return new AuthResponse(token, user.getRole());
        } catch (Exception e) {
            log.error("Authentication failed for username: {} - {}", request.getUsername(), e.getMessage());
            throw new BadCredentialsException("Invalid credentials");
        }
    }
}
