package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User saveUser(User user) {
		// encode password
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public boolean checkPassword(String rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}
	
	// UserService.java
	public Optional<User> findByUsernameOrEmail(String identifier) {
	    // identifier can be username or email
	    Optional<User> userOpt = userRepository.findByUsername(identifier);
	    if (userOpt.isEmpty()) {
	        userOpt = userRepository.findByEmail(identifier);
	    }
	    return userOpt;
	}
	

}
