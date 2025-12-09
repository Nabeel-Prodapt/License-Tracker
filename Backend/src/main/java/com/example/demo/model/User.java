package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@NotBlank
	@Size(max = 100)
	@Column(nullable = false, unique = true)
	private String username; // renamed from "name" to "username"

	@Email
	@NotBlank
	@Size(max = 100)
	@Column(nullable = false, unique = true)
	private String email;

	@NotBlank
	@Column(name = "password_hash", nullable = false, length = 255)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role = Role.ENGINEER; // Default role

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private Timestamp createdAt;

//	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//	private List<Device> devices;
}