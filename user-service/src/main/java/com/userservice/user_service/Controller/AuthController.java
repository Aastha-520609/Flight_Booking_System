package com.userservice.user_service.Controller;

import com.userservice.user_service.DTO.AuthRequest;
import com.userservice.user_service.DTO.AuthResponse;
import com.userservice.user_service.DTO.UserDTO;
import com.userservice.user_service.Entity.User;
import com.userservice.user_service.Service.JwtService;
import com.userservice.user_service.Service.UserService;

import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail()); // Set email field
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

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
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            String token = jwtService.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(new AuthResponse("Invalid username or password"));
        }
    }
}

