package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.PasswordResetService;
import com.example.demo.service.UserService;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class PasswordResetController {

    private final UserService userService;
    private final PasswordResetService passwordResetService; // <-- add this

    public PasswordResetController(UserService userService,             
                                   PasswordResetService passwordResetService) { // <-- inject here
        this.userService = userService;
        this.passwordResetService = passwordResetService; // <-- assign here
    }

    // Step 1: Request password reset
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        // Use PasswordResetService to generate token
        String token = passwordResetService.createResetToken(email);

        if (token == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // Build reset link
        String resetLink = "http://localhost:5173/reset-password?token=" + token;

        // For production: send via email. For testing: return link
        return ResponseEntity.ok("Reset link (for testing): " + resetLink);
    }



    // Step 2: Reset password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");

        // Validate request
        if (token == null || newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body("Token and new password are required");
        }

        // Step 1: Get email associated with the token
        String email = passwordResetService.getEmailByToken(token);
        if (email == null) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }

        // Step 2: Find user by email
        Optional<User> userOpt = userService.findByUsernameOrEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOpt.get();

        // Step 3: Encode and save the new password
        user.setPassword(newPassword);
        userService.saveUser(user); // this updates MySQL

        // Step 4: Invalidate the token so it cannot be reused
        passwordResetService.invalidateToken(token);

        return ResponseEntity.ok("Password reset successful");
    }
}