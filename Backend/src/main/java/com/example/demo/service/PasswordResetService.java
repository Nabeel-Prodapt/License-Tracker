package com.example.demo.service;

import com.example.demo.model.PasswordResetToken;
import com.example.demo.model.User;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;

    public PasswordResetService(PasswordResetTokenRepository tokenRepository,
                                UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a password reset token for the given email.
     * Deletes any existing token for the same user.
     *
     * @param email user's email
     * @return generated token or null if user not found
     */
    public String createResetToken(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return null; // user not found
        }

        User user = userOpt.get();

        // Delete existing token(s) for the user
        tokenRepository.deleteByUser_UserId(user.getUserId());

        // Generate new token
        String token = UUID.randomUUID().toString();
        Instant expiryTime = Instant.now().plus(15, ChronoUnit.MINUTES);

        PasswordResetToken resetToken = new PasswordResetToken(token, user, expiryTime);
        tokenRepository.save(resetToken);

        return token;
    }

    /**
     * Validate the token and return associated user's email.
     *
     * @param token reset token
     * @return email if valid, null if invalid or expired
     */
    public String getEmailByToken(String token) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);

        if (tokenOpt.isEmpty()) {
            return null; // token not found
        }

        PasswordResetToken resetToken = tokenOpt.get();

        // Check if token is expired
        if (resetToken.getExpiryTime().isBefore(Instant.now())) {
            tokenRepository.delete(resetToken); // remove expired token
            return null;
        }

        return resetToken.getUser().getEmail();
    }

    /**
     * Invalidate (delete) a reset token.
     *
     * @param token reset token
     */
    public void invalidateToken(String token) {
        tokenRepository.findByToken(token)
                .ifPresent(tokenRepository::delete);
    }
}