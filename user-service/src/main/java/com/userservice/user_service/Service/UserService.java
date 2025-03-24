package com.userservice.user_service.Service;

import com.userservice.user_service.Entity.Role;
import com.userservice.user_service.Entity.User;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService{
    User registerUser(User user);
    Optional<User> findByUsername(String username);
    Role getUserRole(String username); 
}