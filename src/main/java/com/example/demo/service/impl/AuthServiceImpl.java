package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.exception.BadCredentialsException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.util.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User validateCredentials(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return user;
    }

    @Override
    public void validatePassword(String rawPassword, String hashedPassword) {
        if (!passwordEncoder.matches(rawPassword, hashedPassword)) {
            throw new BadCredentialsException("Invalid credentials");
        }
    }
}
