package com.userservice.user_service.Controller;

import com.userservice.user_service.DTO.AuthRequest;
import com.userservice.user_service.DTO.AuthResponse;
import com.userservice.user_service.DTO.UserDTO;
import com.userservice.user_service.Entity.User;
import com.userservice.user_service.Service.JwtService;
import com.userservice.user_service.Service.UserService;

import jakarta.validation.Valid;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("API is working!");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setPassword(encodedPassword);

            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully with username: " + registeredUser.getUsername());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: Username or email already exists.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred: " + e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest authRequest) {
        try {
            Optional<User> optionalUser = userService.findByUsername(authRequest.getUsername());

            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(401).body(new AuthResponse("Invalid username or password"));
            }

            User user = optionalUser.get();

            if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.status(401).body(new AuthResponse("Invalid username or password"));
            }

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtService.generateToken(authentication.getName());

            return ResponseEntity.ok(new AuthResponse(token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(new AuthResponse("Invalid username or password"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AuthResponse("Error: " + e.getMessage()));
        }
    }
}
