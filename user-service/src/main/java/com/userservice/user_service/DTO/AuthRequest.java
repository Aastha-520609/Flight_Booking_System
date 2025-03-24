package com.userservice.user_service.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequest {
	@NotNull(message = "Username cannot be null")
	@Size(min = 6, message = "Username must be at least 6 characters")
    private String username;
	
	@NotNull(message = "Password cannot be null")
	@Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
