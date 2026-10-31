package com.example.demo.service;

import com.example.demo.entity.User;

public interface AuthService {
    User validateCredentials(String username, String password);
    void validatePassword(String rawPassword, String hashedPassword);
}
