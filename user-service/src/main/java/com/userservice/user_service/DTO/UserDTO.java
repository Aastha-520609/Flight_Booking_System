package com.userservice.user_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	@NotNull(message = "UserName cannot be null")
    private String username;
	
	@NotNull(message = "Email cannot be null")
	private String email;
	
	@NotNull(message = "Password cannot be null")
    private String password;
}