package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.SignupRequest;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/auth")
public class AuthController {

	private final UserService userService;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder;

	public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
		this.passwordEncoder = passwordEncoder;
	}

	// -------------------- SIGNUP --------------------
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
		if (userService.existsByUsername(request.getUsername())) {
			return ResponseEntity.badRequest().body("Username already exists");
		}
		if (userService.existsByEmail(request.getEmail())) {
			return ResponseEntity.badRequest().body("Email already in use");
		}

		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword()); // 👈 raw password, let service encode
		user.setRole(Role.valueOf(request.getRole().toUpperCase()));

		userService.saveUser(user);
		return ResponseEntity.ok("User registered successfully");
	}

	// -------------------- LOGIN --------------------
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
		var userOpt = userService.findByUsername(request.getUsername());
		if (userOpt.isEmpty()) {
			return ResponseEntity.status(401).body("Invalid credentials");
		}

		var user = userOpt.get();
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			return ResponseEntity.status(401).body("Invalid credentials");
		}

		// Generate JWT with username and role
		String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

		return ResponseEntity.ok(new AuthResponse(token));
	}
}