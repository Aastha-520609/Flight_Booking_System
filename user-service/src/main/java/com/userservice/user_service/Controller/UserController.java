package com.userservice.user_service.Controller;

import com.userservice.user_service.DTO.UserDTO;
import com.userservice.user_service.Entity.User;
import com.userservice.user_service.Service.UserService;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOptional = userService.findByUsername(userDetails.getUsername());

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        return ResponseEntity.ok(userDTO);
    }

}

