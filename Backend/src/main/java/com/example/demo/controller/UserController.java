package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/user")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/details")
	public ResponseEntity<?> getUserDetails(Authentication authentication) {
		String username = authentication.getName(); // ✅ Works with correct import
		Optional<User> user = userService.findByUsername(username);

		return user.<ResponseEntity<?>>map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.badRequest().body("User not found"));
	}
}
