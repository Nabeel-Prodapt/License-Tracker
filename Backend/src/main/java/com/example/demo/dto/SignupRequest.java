package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignupRequest {

	@NotBlank
	private String username;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String password;

	@NotNull
	private String role; // String that will be converted to Role enum
}