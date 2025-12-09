// PasswordResetToken.java
package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Instant expiryTime;

    @ManyToOne
    private User user;

    public PasswordResetToken() {}
    public PasswordResetToken(String token, User user, Instant expiryTime) {
        this.token = token;
        this.user = user;
        this.expiryTime = expiryTime;
    }
}